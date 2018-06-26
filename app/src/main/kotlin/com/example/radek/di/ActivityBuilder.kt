package com.example.radek.di

import com.example.radek.di.module.MovieDetailsModule
import com.example.radek.di.module.MovieListModule
import com.example.radek.di.scope.ActivityScope
import com.example.radek.moviedetail.MovieDetailsActivity
import com.example.radek.movielist.MovieListActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MovieListModule::class])
    internal abstract fun bindMovieListActivity(): MovieListActivity


    @ActivityScope
    @ContributesAndroidInjector(modules = [MovieDetailsModule::class])
    internal abstract fun bindMovieDetailsActivity(): MovieDetailsActivity
}
