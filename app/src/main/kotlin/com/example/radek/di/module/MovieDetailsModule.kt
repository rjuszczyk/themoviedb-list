package com.example.radek.di.module

import com.example.radek.di.scope.ActivityScope
import com.example.radek.model.provider.MovieDetailsProvider
import com.example.radek.moviedetail.MovieDetailsViewModelFactoryCreator
import com.example.radek.moviedetail.data.MovieDetailsProviderImpl
import com.example.radek.movielist.data.network.Api
import dagger.Module
import dagger.Provides

@Module
class MovieDetailsModule {

    @Provides
    @ActivityScope
    fun provideMovieDetailProvider(api:Api) :MovieDetailsProvider {
        return MovieDetailsProviderImpl(api)
    }

    @Provides
    @ActivityScope
    fun provideMovieDetailsViewModelFactoryCreator(movieDetailsProvider: MovieDetailsProvider): MovieDetailsViewModelFactoryCreator {
        return MovieDetailsViewModelFactoryCreator(movieDetailsProvider)
    }
}