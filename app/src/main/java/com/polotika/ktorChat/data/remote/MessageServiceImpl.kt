package com.polotika.ktorChat.data.remote

import com.polotika.ktorChat.data.remote.dto.MessageDto
import com.polotika.ktorChat.domain.model.Message
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.request.*

/*
import io.ktor.client.request.*
*/

class MessageServiceImpl(private val client: HttpClient):MessageService {

    override suspend fun getAllMessages(): List<Message> {

        return try {
            client.get<List<MessageDto>>(MessageService.EndPoints.GetAllMessages.url).map {
                it.toMessage()
            }
        }catch (e:Exception){
            emptyList()
        }
    }
}