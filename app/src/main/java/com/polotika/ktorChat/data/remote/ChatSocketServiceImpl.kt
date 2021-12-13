package com.polotika.ktorChat.data.remote

import com.polotika.ktorChat.data.remote.dto.MessageDto
import com.polotika.ktorChat.domain.model.Message
import com.polotika.ktorChat.utils.Resource
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ChatSocketServiceImpl(private val client: HttpClient) : ChatSocketService {

    private var socket: WebSocketSession? = null
    override suspend fun initSession(userName: String): Resource<Unit> {
        return try {
                socket = client.webSocketSession{
                    url("${ChatSocketService.EndPoints.ChatSocket.url}?username=$userName")
                }
                if (socket?.isActive == true){
                    Resource.Success(Unit)
                }else{
                    Resource.Error("Couldn't establish a connection")
                }

            Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage)
        }
    }

    override suspend fun sendMessage(message: String) {
        try {
            socket?.send(Frame.Text(message))
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    override fun observeMessages(): Flow<Message> {
        return try {
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter {
                    it is Frame.Text
                }
                ?.map {
                    val json = (it as Frame.Text).readText()
                    val messageDto = Json.decodeFromString<MessageDto>(json)
                    messageDto.toMessage()
                }?: flow {  }
        } catch (e: Exception) {
            e.printStackTrace()
            flow { }
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }
}