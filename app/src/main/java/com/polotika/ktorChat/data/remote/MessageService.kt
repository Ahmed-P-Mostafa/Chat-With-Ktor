package com.polotika.ktorChat.data.remote

import com.polotika.ktorChat.domain.model.Message

interface MessageService {
    suspend fun getAllMessages():List<Message>

    companion object{
        const val BASE_URL = "http://192.168.1.12:8083"
    }

    sealed class EndPoints(val url:String){
        object GetAllMessages:EndPoints("$BASE_URL/messages")
    }
}