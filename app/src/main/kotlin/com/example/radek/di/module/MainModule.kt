package com.example.radek.di.module

import com.example.radek.di.scope.ActivityScope
import com.example.radek.jobexecutor.PageProviderExecutor
import com.example.radek.jobexecutor.PagedDataProvider
import com.example.radek.movielist.*
import com.example.radek.movielist.model.MovieItem
import com.example.radek.network.Api
import com.example.radek.data.MovieListPagedDataProvider
import com.example.radek.data.SortOptionsProvider
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor

@Module
class MainModule {

    @Provides
    @ActivityScope
    fun provideMainNetworkRepository(
            pagedDataProvider: PagedDataProvider<MovieItem>
    ): PageProviderExecutor<MovieItem> {
        return PageProviderExecutor(pagedDataProvider)
    }


    @Provides
    @ActivityScope
    fun providePagedDataProvider(api: Api): PagedDataProvider<MovieItem> {
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
            pageProviderExecutor: PageProviderExecutor<MovieItem>,
            mainThreadExecutor: Executor
    ): MainViewModelFactory {
        return MainViewModelFactory(sortOptionsProvider, movieListPagedDataProviderFactory, pageProviderExecutor, mainThreadExecutor)
    }
}