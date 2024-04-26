package com.example.articleappassignment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.articleappassignment.models.NewsResponse
import com.example.articleappassignment.repository.NewsRepository
import com.example.articleappassignment.utils.Constants.Companion.API_RESPONSE_ERROR
import com.example.articleappassignment.utils.Constants.Companion.API_URL
import com.example.articleappassignment.utils.Result
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing news-related data and operations.
 *
 * This ViewModel interacts with a repository to fetch news articles and exposes the result
 * through LiveData for observation by UI components.
 *
 * @param repository The repository responsible for providing news data.
 */
class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    /** LiveData holding the news response data. */
    val newsLiveData = MutableLiveData<NewsResponse?>()


    /**
     * Asynchronously fetches news articles from the repository and updates the [newsLiveData].
     *
     * This function launches a coroutine in the [viewModelScope] to perform the API call asynchronously.
     * Upon receiving the response from the repository, it updates the [newsLiveData] with the received
     * data in case of success, or with `null` in case of an error.
     * Any exceptions that occur during the operation are logged and `null` is posted to [newsLiveData].
     */
    fun fetchNewsArticles() {
        viewModelScope.launch {
            try {
                // Make API call using repository
                when (val response = repository.fetchNewsArticles(API_URL)) {
                    is Result.Success -> {
                        val newsResponse = response.data
                        newsLiveData.postValue(newsResponse)
                    }

                    is Result.Error -> {
                        val errorMessage = response.message
                        Log.e(API_RESPONSE_ERROR, errorMessage)
                        newsLiveData.postValue(null)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                newsLiveData.postValue(null)
            }
        }
    }
}