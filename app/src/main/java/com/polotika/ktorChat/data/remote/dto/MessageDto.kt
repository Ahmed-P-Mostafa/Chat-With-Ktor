package com.polotika.ktorChat.data.remote.dto

import com.polotika.ktorChat.domain.model.Message
import java.text.DateFormat
import java.util.*

data class MessageDto(val id:String,val text:String,val timeStamp:Long,val userName:String) {

    fun toMessage():Message{
        val date = Date(timeStamp)
        val formattedTime = DateFormat.getDateInstance(DateFormat.DEFAULT).format(date)
        return Message(formattedTime = formattedTime,text, userName)
    }
}