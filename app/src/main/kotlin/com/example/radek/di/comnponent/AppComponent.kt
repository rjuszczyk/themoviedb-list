package com.example.radek.di.comnponent

import com.example.radek.MovieListApplication
import com.example.radek.di.binding.BindingModule
import com.example.radek.di.modules.AppModule
import com.example.radek.di.modules.NetworkModule
import com.example.radek.di.scope.AppScope
import dagger.Component
import dagger.android.AndroidInjectionModule

@Component(modules = [AndroidInjectionModule::class, BindingModule::class, AppModule::class, NetworkModule::class])
@AppScope
interface AppComponent {
    fun inject(myApplication: MovieListApplication)
}