package com.polotika.ktorChat.data.remote

import com.polotika.ktorChat.domain.model.Message
import com.polotika.ktorChat.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {

    suspend fun initSession(userName:String):Resource<Unit>

    suspend fun sendMessage(message: String)

    fun observeMessages():Flow<Message>

    suspend fun closeSession()


    companion object{
        //const val BASE_URL = "ws://192.168.1.12:8082"
        const val BASE_URL = "ws://localhost:8082"
    }

    sealed class EndPoints(val url:String){
        object ChatSocket:EndPoints("$BASE_URL/chat-socket")
    }
}