package com.example.radek.di.comnponent

//import com.example.radek.di.binding.BindingModule
import com.example.radek.MovieListApplication
import com.example.radek.di.ActivityBuilder
import com.example.radek.di.module.AppBindingsModule
import com.example.radek.di.module.AppModule
import com.example.radek.di.module.NetworkModule
import com.example.radek.di.scope.AppScope
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    AppBindingsModule::class,
    ActivityBuilder::class,
    NetworkModule::class
])

@AppScope
interface AppComponent : AndroidInjector<MovieListApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MovieListApplication>()
}
