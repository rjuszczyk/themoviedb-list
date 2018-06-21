package com.example.radek.di.module

import android.content.Context
import android.os.Handler
import com.example.radek.MovieListApplication
import dagger.Module
import javax.inject.Named
import dagger.Binds
import dagger.Provides
import java.util.concurrent.Executor
import javax.inject.Singleton


@Module
abstract class AppBindingsModule {

    @Binds
    @Named("AppContext")
    internal abstract fun provideAppContext(app: MovieListApplication): Context
}