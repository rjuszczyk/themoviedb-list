package com.example.radek.di.comnponent

import com.example.radek.MovieListApplication
import com.example.radek.di.module.ActivityBuilder
import com.example.radek.di.module.AppModule
import com.example.radek.di.module.ExecutorModule
import com.example.radek.di.module.NetworkModule
import com.example.radek.di.scope.AppScope
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Component(modules = [
    AndroidSupportInjectionModule::class,
    ExecutorModule::class,
    AppModule::class,
    ActivityBuilder::class,
    NetworkModule::class
])

@AppScope
interface AppComponent : AndroidInjector<MovieListApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MovieListApplication>()
}
