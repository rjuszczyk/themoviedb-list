package com.example.radek.di.module

import com.example.radek.data.MoviesPageProviderImpl
import com.example.radek.data.SortOptionsProviderImpl
import com.example.radek.data.network.Api
import com.example.radek.di.scope.ActivityScope
import com.example.radek.jobexecutor.PageProviderExecutor
import com.example.radek.model.MovieItem
import com.example.radek.model.provider.MoviesPageProvider
import com.example.radek.model.provider.SortOptionsProvider
import com.example.radek.movielist.MovieListViewModelFactory
import com.example.radek.movielist.MovieListPagedDataProviderFactory
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor

@Module
class MainModule {

    @Provides
    @ActivityScope
    fun provideMainNetworkRepository(): PageProviderExecutor<MovieItem> {
        return PageProviderExecutor()
    }

    @Provides
    @ActivityScope
    fun provideMoviesPageProvider(api: Api): MoviesPageProvider {
        return MoviesPageProviderImpl(api)
    }

    @Provides
    @ActivityScope
    fun provideMovieListPagedDataProviderFactory(moviesPageProvider: MoviesPageProvider): MovieListPagedDataProviderFactory {
        return MovieListPagedDataProviderFactory(moviesPageProvider)
    }

    @Provides
    @ActivityScope
    fun provideSortOptionsProvider() : SortOptionsProvider = SortOptionsProviderImpl()

    @Provides
    @ActivityScope
    fun provideMainViewModelFactory(
            sortOptionsProvider: SortOptionsProvider,
            movieListPagedDataProviderFactory: MovieListPagedDataProviderFactory,
            pageProviderExecutor: PageProviderExecutor<MovieItem>,
            mainThreadExecutor: Executor
    ): MovieListViewModelFactory {
        return MovieListViewModelFactory(sortOptionsProvider, movieListPagedDataProviderFactory, pageProviderExecutor, mainThreadExecutor)
    }
}