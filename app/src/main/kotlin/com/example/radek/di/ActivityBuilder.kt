package com.example.radek.di

import com.example.radek.di.module.MainModule
import com.example.radek.di.scope.ActivityScope
import com.example.radek.movielist.MovieListActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun bindMainActivity(): MovieListActivity
}
