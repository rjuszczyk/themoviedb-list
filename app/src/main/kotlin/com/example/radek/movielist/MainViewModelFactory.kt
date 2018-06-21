package com.example.radek.movielist

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.radek.data.SortOptionsProvider
import com.example.radek.jobexecutor.PageProviderExecutor
import com.example.radek.movielist.model.MovieItem
import java.util.concurrent.Executor

class MainViewModelFactory(
        private val sortOptionsProvider: SortOptionsProvider,
        private val movieListPagedDataProviderFactory: MovieListPagedDataProviderFactory,
        private val pageProviderExecutor: PageProviderExecutor<MovieItem>,
        private val mainThreadExecutor: Executor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(sortOptionsProvider, movieListPagedDataProviderFactory, pageProviderExecutor, mainThreadExecutor) as T
    }
}
