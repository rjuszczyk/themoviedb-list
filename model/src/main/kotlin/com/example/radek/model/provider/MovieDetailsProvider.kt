package com.example.radek.model.provider

import com.example.radek.model.MovieDetailsItem

interface MovieDetailsProvider {
    fun provideMovieDetails(movieItemId: Int, callback: Callback)

    interface Callback {
        fun onSuccess(movieDetailsItem: MovieDetailsItem)
        fun onFailed(throwable: Throwable)
    }
}
