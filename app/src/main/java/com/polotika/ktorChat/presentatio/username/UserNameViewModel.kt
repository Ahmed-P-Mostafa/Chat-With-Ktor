package com.polotika.ktorChat.presentatio.username

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserNameViewModel @Inject constructor() :ViewModel(){

    private val _userNameText = mutableStateOf("")
    val userNameText : State<String> = _userNameText

    private val _onJoinChat = MutableSharedFlow<String>()
    val onJoinChat = _onJoinChat.asSharedFlow()


    fun onUserNameChanged(userName:String){
        _userNameText.value = userName
    }

    fun onJoinCLick(){
        viewModelScope.launch {
            if (userNameText.value.isNotBlank()){
                _onJoinChat.emit(userNameText.value)
            }
        }
    }
}