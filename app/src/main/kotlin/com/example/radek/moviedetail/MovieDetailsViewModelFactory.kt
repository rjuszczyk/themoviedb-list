package com.example.radek.moviedetail

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.radek.model.provider.MovieDetailProvider

class MovieDetailsViewModelFactory(
        private val movieDetailProvider: MovieDetailProvider,
        private val movieItemId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MovieDetailsViewModel(movieDetailProvider, movieItemId) as T
    }
}
