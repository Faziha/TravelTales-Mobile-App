package com.example.travel_tales_xml.models

data class Comment(
    val id: String,
    val articleId: String,
    val authorId: String,
    val comment: String,
    val timeStamp: String
)
