package com.polotika.ktorChat.presentatio.chat

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polotika.ktorChat.data.remote.ChatSocketService
import com.polotika.ktorChat.data.remote.MessageService
import com.polotika.ktorChat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageService: MessageService,
    private val chatSocketService: ChatSocketService,
    private val savedHandle: SavedStateHandle
) : ViewModel() {
    fun connectToChat() {
        getAllMessages()
        savedHandle.get<String>("username")?.let { username->
            viewModelScope.launch {
                when(val result = chatSocketService.initSession(username)){
                    is Resource.Success->{
                        chatSocketService.observeMessages().onEach { message ->
                            val newList = state.value.messages.toMutableList().apply {
                                add(0,message)
                            }
                            _state.value = state.value.copy(messages = newList)
                        }.launchIn(viewModelScope)
                    }
                    is Resource.Error ->{
                        _toastEvent.emit(result.message?:"Unknown error")
                    }
                }
            }
        }
    }


    private val _messageText = mutableStateOf("")
    val messageText : State<String> = _messageText

    private val _state = mutableStateOf(ChatState())
    val state :State<ChatState> = _state

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    private fun getAllMessages(){
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            val results = messageService.getAllMessages()
            _state.value = state.value.copy(isLoading = false, messages = results)
        }
    }

    fun onMessageChange(message:String){
        _messageText.value = message
    }

    fun sendMessage(){
        if (_messageText.value.isNotBlank()){
            viewModelScope.launch {
                chatSocketService.sendMessage(messageText.value)
            }
        }
    }

    fun disconnect(){
        viewModelScope.launch {
            chatSocketService.closeSession()
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}