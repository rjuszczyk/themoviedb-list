package com.example.radek.di.module

import com.example.radek.di.scope.ActivityScope
import com.example.radek.model.provider.MovieDetailProvider
import com.example.radek.moviedetail.MovieDetailsViewModelFactoryCreator
import com.example.radek.movielist.data.MovieDetailProviderImpl
import com.example.radek.movielist.data.network.Api
import dagger.Module
import dagger.Provides

@Module
class MovieDetailsModule {

    @Provides
    @ActivityScope
    fun provideMovieDetailProvider(api:Api) :MovieDetailProvider {
        return MovieDetailProviderImpl(api)
    }

    @Provides
    @ActivityScope
    fun provideMovieDetailsViewModelFactoryCreator(movieDetailProvider: MovieDetailProvider): MovieDetailsViewModelFactoryCreator {
        return MovieDetailsViewModelFactoryCreator(movieDetailProvider)
    }
}