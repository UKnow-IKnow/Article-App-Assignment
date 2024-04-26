package com.example.articleappassignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.articleappassignment.repository.NewsRepository


class NewsViewModelFactory(private val repository: NewsRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}