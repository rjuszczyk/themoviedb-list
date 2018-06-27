package com.example.radek.moviedetail

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.radek.model.provider.MovieDetailsProvider

class MovieDetailsViewModelFactory(
        private val movieDetailsProvider: MovieDetailsProvider,
        private val movieItemId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MovieDetailsViewModel(movieDetailsProvider, movieItemId) as T
    }
}
