package com.example.articleappassignment.utils

/**
 * A sealed class representing the result of an operation, which can be either success or failure.
 *
 * @param T The type of data encapsulated within the result in case of success.
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}