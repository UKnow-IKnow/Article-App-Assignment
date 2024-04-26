package com.example.articleappassignment.repository

import com.example.articleappassignment.models.NewsResponse
import com.example.articleappassignment.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Repository class for fetching news articles from a remote API.
 * This class provides a method to fetch news articles asynchronously.
 */
class NewsRepository {

    /**
     * Fetches news articles from the specified API URL.
     * @param apiUrl The URL of the API from which news articles are to be fetched.
     * @return A Result object containing either the fetched news articles or an error message.
     */
    suspend fun fetchNewsArticles(apiUrl: String): Result<NewsResponse> {
        // Execute the following block of code in the IO dispatcher context
        return withContext(Dispatchers.IO) {
            try {
                // Create a URL object from the provided API URL
                val url = URL(apiUrl)
                // Open a connection to the URL
                val connection = url.openConnection() as HttpURLConnection

                // Set the request method to GET
                connection.requestMethod = "GET"
                // Get the HTTP response code
                val responseCode = connection.responseCode

                // If the response code indicates success (HTTP_OK)
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response data from the input stream
                    val reader = BufferedReader(InputStreamReader(connection.inputStream, "UTF-8"))
                    val response = StringBuilder()
                    var line: String?

                    while (reader.readLine().also { line = it } != null) {
                        response.append(line).append("\n")
                    }

                    reader.close()
                    // Parse the JSON response using Gson
                    val gson = Gson()
                    val newsResponse = gson.fromJson(response.toString(), NewsResponse::class.java)

                    // Return a Result.Success with the parsed news response
                    Result.Success(newsResponse)
                } else {
                    // If the response code is not HTTP_OK, return a Result.Error with the HTTP error code
                    Result.Error("HTTP Error: $responseCode")
                }
            } catch (e: Exception) {
                // If an exception occurs during network operation, return a Result.Error with the error message
                Result.Error("Network error: ${e.message}")
            }
        }
    }

}