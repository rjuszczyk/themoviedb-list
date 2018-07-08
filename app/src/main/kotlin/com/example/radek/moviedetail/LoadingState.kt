package com.example.radek.moviedetail

sealed class LoadingState {
    object NotStarted : LoadingState()
    object Loading : LoadingState()
    object Loaded : LoadingState()
    class Failed(val cause:Throwable) : LoadingState()
}