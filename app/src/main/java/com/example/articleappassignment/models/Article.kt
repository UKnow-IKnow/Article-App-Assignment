package com.example.articleappassignment.models

/**
 * Data class representing a news article.
 * @property author The author of the article.
 * @property content The content of the article.
 * @property description The description of the article.
 * @property publishedAt The published date and time of the article.
 * @property source The source of the article.
 * @property title The title of the article.
 * @property url The URL of the article.
 * @property urlToImage The URL to the image associated with the article.
 */
data class Article(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
)