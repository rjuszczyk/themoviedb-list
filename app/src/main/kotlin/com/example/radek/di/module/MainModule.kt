package com.example.radek.di.module

import com.example.radek.di.scope.ActivityScope
import com.example.radek.jobexecutor.MainNetworkRepository
import com.example.radek.jobexecutor.PagedDataProvider
import com.example.radek.movielist.*
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor

@Module
class MainModule {

    @Provides
    @ActivityScope
    fun provideMainNetworkRepository(
            pagedDataProvider: PagedDataProvider<NetResult>
    ): MainNetworkRepository<NetResult> {
        return MainNetworkRepository(pagedDataProvider)
    }


    @Provides
    @ActivityScope
    fun providePagedDataProvider(api: Api): PagedDataProvider<NetResult> {
        return MovieListPagedDataProvider(api)
    }

    @Provides
    @ActivityScope
    fun provideMovieListPagedDataProviderFactory(api: Api): MovieListPagedDataProviderFactory {
        return MovieListPagedDataProviderFactory(api)
    }

    @Provides
    @ActivityScope
    fun provideSortOptionsProvider() = SortOptionsProvider()

    @Provides
    @ActivityScope
    fun provideMainViewModelFactory(
            sortOptionsProvider: SortOptionsProvider,
            movieListPagedDataProviderFactory: MovieListPagedDataProviderFactory,
            mainNetworkRepository: MainNetworkRepository<NetResult>,
            mainThreadExecutor: Executor
    ): MainViewModelFactory {
        return MainViewModelFactory(sortOptionsProvider, movieListPagedDataProviderFactory, mainNetworkRepository, mainThreadExecutor)
    }
}