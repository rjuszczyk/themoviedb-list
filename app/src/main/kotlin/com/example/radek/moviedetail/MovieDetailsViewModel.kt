package com.example.radek.moviedetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.radek.model.MovieDetailsItem
import com.example.radek.model.provider.MovieDetailProvider

class MovieDetailsViewModel(
        private val movieDetailProvider: MovieDetailProvider,
        private val movieItemId: Int
) : ViewModel() {
    val movieDetailsItem = MutableLiveData<MovieDetailsItem>()
    val state = MutableLiveData<State>()
    init {
        state.value = State.NotStarted
        loadMovieDetails()
    }

    private fun loadMovieDetails() {
        state.postValue(State.Loading)
        movieDetailProvider.provideMovieDetails(movieItemId, object : MovieDetailProvider.Callback {
            override fun onSuccess(movieDetailsItem: MovieDetailsItem) {
                this@MovieDetailsViewModel.movieDetailsItem.postValue(movieDetailsItem)
                state.postValue(State.Loaded)
            }

            override fun onFailed(throwable: Throwable) {
                state.postValue(State.Failed(throwable))
            }
        })
    }

    fun retryLoading() {
        loadMovieDetails()
    }
}
