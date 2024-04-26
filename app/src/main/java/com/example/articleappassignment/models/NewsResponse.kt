package com.example.articleappassignment.models

/**
 * Data class representing the response from a news API.
 * @property articles The list of articles returned in the response.
 * @property status The status of the response.
 */
data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String
)