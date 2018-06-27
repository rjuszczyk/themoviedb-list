package com.example.radek.moviedetail

import com.example.radek.model.provider.MovieDetailsProvider

class MovieDetailsViewModelFactoryCreator(
        private val movieDetailsProvider: MovieDetailsProvider
) {
    fun create(movieItemId: Int) : MovieDetailsViewModelFactory {
        return MovieDetailsViewModelFactory(movieDetailsProvider, movieItemId)
    }
}
