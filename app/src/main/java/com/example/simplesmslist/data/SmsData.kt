package com.example.simplesmslist.data

import java.util.Date

data class SmsData (
    val address: String,
    val messages: List<SmsMessage>
)


data class SmsMessage (
    val address: String,
    val date: Date,
    val content: String
)
