package com.example.radek.movielist

import com.example.radek.movielist.data.MoviesPageDataProviderImpl
import com.example.radek.movielist.data.SortOptionsProviderImpl
import com.example.radek.movielist.data.network.Api
import com.example.radek.di.scope.ActivityScope
import com.example.radek.jobexecutor.PageProviderExecutor
import com.example.radek.model.MovieItem
import com.example.radek.model.provider.MoviesPageDataProvider
import com.example.radek.model.provider.SortOptionsProvider
import com.example.radek.movielist.data.MovieListPagedDataProviderFactory
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor

@Module
class MovieListModule {

    @Provides
    @ActivityScope
    fun providePageProviderExecutor(): PageProviderExecutor<MovieItem> {
        return PageProviderExecutor()
    }

    @Provides
    @ActivityScope
    fun provideMoviesPageProvider(api: Api): MoviesPageDataProvider {
        return MoviesPageDataProviderImpl(api)
    }

    @Provides
    @ActivityScope
    fun provideMovieListPagedDataProviderFactory(moviesPageDataProvider: MoviesPageDataProvider): MovieListPagedDataProviderFactory {
        return MovieListPagedDataProviderFactory(moviesPageDataProvider)
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