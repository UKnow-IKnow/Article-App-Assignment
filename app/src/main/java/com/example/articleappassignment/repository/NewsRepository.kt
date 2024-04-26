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

class NewsRepository {

    suspend fun fetchNewsArticles(apiUrl: String): Result<NewsResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"
                val responseCode = connection.responseCode


                if (responseCode == HttpURLConnection.HTTP_OK) {

                    val reader = BufferedReader(InputStreamReader(connection.inputStream, "UTF-8"))
                    val response = StringBuilder()
                    var line: String?

                    while (reader.readLine().also { line = it } != null) {
                        response.append(line).append("\n")
                    }

                    reader.close()
                    val gson = Gson()
                    val newsResponse = gson.fromJson(response.toString(), NewsResponse::class.java)

                    Result.Success(newsResponse)
                } else {
                    Result.Error("HTTP Error: $responseCode")
                }
            } catch (e: Exception) {
                Result.Error("Network error: ${e.message}")
            }
        }
    }

}