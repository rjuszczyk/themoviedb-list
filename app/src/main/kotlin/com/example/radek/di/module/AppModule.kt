package com.example.radek.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import javax.inject.Named
import dagger.Binds


@Module
abstract class AppModule {

    @Binds
    @Named("AppContext")
    internal abstract fun provideAppContext(app: Application): Context
}