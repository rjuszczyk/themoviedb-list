package com.example.radek.di.binding

import android.app.Activity
import com.example.radek.di.comnponent.MainComponent

import com.example.radek.movielist.MainActivity

import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = [MainComponent::class])
abstract class BindingModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun bindMainActivityInjectorFactory(builder: MainComponent.Builder): AndroidInjector.Factory<out Activity>
}