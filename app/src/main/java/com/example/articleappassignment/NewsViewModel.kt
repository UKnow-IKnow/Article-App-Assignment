package com.example.articleappassignment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.articleappassignment.models.NewsResponse
import com.example.articleappassignment.repository.NewsRepository
import com.example.articleappassignment.utils.Constants.Companion.API_RESPONSE_ERROR
import com.example.articleappassignment.utils.Constants.Companion.API_URL
import kotlinx.coroutines.launch
import com.example.articleappassignment.utils.Result

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    val newsLiveData = MutableLiveData<NewsResponse?>()


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
                        Log.e(API_RESPONSE_ERROR,errorMessage)
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