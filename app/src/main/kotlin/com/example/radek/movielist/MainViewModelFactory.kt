package com.example.radek.movielist

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.radek.jobexecutor.MainNetworkRepository
import java.util.concurrent.Executor

class MainViewModelFactory(
        private val sortOptionsProvider: SortOptionsProvider,
        private val movieListPagedDataProviderFactory: MovieListPagedDataProviderFactory,
        private val mainNetworkRepository: MainNetworkRepository<NetResult>,
        private val mainThreadExecutor: Executor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(sortOptionsProvider, movieListPagedDataProviderFactory, mainNetworkRepository, mainThreadExecutor) as T
    }
}
