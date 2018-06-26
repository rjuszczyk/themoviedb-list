package com.example.radek.moviedetail

import com.example.radek.model.provider.MovieDetailProvider

class MovieDetailsViewModelFactoryCreator(
        private val movieDetailProvider: MovieDetailProvider
) {
    fun create(movieItemId: Int) : MovieDetailsViewModelFactory {
        return MovieDetailsViewModelFactory(movieDetailProvider, movieItemId)
    }
}
