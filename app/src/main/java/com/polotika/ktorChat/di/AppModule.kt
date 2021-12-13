package com.polotika.ktorChat.di

import com.polotika.ktorChat.data.remote.ChatSocketService
import com.polotika.ktorChat.data.remote.ChatSocketServiceImpl
import com.polotika.ktorChat.data.remote.MessageService
import com.polotika.ktorChat.data.remote.MessageServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.JsonFeature.Feature.install
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import kotlinx.serialization.serializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO){
            install(Logging)
            install(WebSockets)
            install(JsonFeature){
                serializer = KotlinxSerializer()
            }
        }
    }

    @Singleton
    @Provides
    fun provideMessageService(client:HttpClient):MessageService{
        return MessageServiceImpl(client)
    }

    @Singleton
    @Provides
    fun provideChatSocketService(client:HttpClient):ChatSocketService{
        return ChatSocketServiceImpl(client)
    }
}