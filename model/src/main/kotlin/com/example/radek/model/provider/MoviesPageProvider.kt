package com.example.radek.model.provider

import com.example.radek.model.MoviesPage
import com.example.radek.model.SortOptionParameter

interface MoviesPageProvider {
    fun provideMoviePage(page:Int, sortOption: SortOptionParameter, callback: Callback): Cancelable

    interface Cancelable {
        fun cancel()
    }

    interface Callback {
        fun onSuccess(cancelable: Cancelable, moviesPage: MoviesPage)
        fun onFailed(cancelable: Cancelable, throwable: Throwable)
    }
}
