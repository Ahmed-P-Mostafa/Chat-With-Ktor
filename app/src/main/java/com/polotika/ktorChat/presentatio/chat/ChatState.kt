package com.polotika.ktorChat.presentatio.chat

import com.polotika.ktorChat.domain.model.Message

data class ChatState(
    val messages :List<Message> = emptyList(),
    val isLoading:Boolean = false
)
