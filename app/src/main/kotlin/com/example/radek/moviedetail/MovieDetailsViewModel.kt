package com.example.radek.moviedetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.radek.model.MovieDetailsItem
import com.example.radek.model.provider.MovieDetailsProvider

class MovieDetailsViewModel(
        private val movieDetailsProvider: MovieDetailsProvider,
        private val movieItemId: Int
) : ViewModel() {
    val movieDetailsItem = MutableLiveData<MovieDetailsItem>()
    val loadingState = MutableLiveData<LoadingState>()
    init {
        loadingState.value = LoadingState.NotStarted
        loadMovieDetails()
    }

    private fun loadMovieDetails() {
        loadingState.postValue(LoadingState.Loading)
        movieDetailsProvider.provideMovieDetails(movieItemId, object : MovieDetailsProvider.Callback {
            override fun onSuccess(movieDetailsItem: MovieDetailsItem) {
                this@MovieDetailsViewModel.movieDetailsItem.postValue(movieDetailsItem)
                loadingState.postValue(LoadingState.Loaded)
            }

            override fun onFailed(throwable: Throwable) {
                loadingState.postValue(LoadingState.Failed(throwable))
            }
        })
    }

    fun retryLoading() {
        loadMovieDetails()
    }
}
