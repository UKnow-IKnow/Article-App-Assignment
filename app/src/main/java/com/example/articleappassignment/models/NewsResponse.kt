package com.example.articleappassignment.models

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String
)