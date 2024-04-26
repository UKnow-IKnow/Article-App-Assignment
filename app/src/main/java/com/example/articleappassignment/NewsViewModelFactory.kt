package com.example.articleappassignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.articleappassignment.repository.NewsRepository


/**
 * Factory class responsible for creating instances of [NewsViewModel].
 *
 * This factory implements the [ViewModelProvider.Factory] interface to create instances of the
 * [NewsViewModel] class with the provided [NewsRepository].
 *
 * @param repository The repository responsible for providing news data.
 */
class NewsViewModelFactory(private val repository: NewsRepository) : ViewModelProvider.Factory {


    /**
     * Creates a new instance of the specified [ViewModel] class.
     *
     * @param modelClass The class of the ViewModel to be created.
     * @return A new instance of the ViewModel.
     * @throws IllegalArgumentException If the provided modelClass is not assignable from [NewsViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return NewsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}