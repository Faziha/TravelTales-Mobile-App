package com.example.travel_tales_xml.models

data class Article(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val authorId: String,
    val likesNo: Int
)

