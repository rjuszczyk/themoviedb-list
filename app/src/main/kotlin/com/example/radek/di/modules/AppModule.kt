package com.example.radek.di.modules

import android.app.Application
import android.content.Context
import com.example.radek.di.scope.AppScope
import dagger.Module
import javax.inject.Named

@Module
class AppModule(private val application: Application) {
    @Named("AppContext")
    @AppScope
    fun provide() : Context {
        return application
    }
}