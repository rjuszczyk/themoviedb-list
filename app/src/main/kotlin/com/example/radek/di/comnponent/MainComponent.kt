package com.example.radek.di.comnponent

import com.example.radek.di.modules.MainModule
import com.example.radek.di.scope.ActivityScope
import com.example.radek.movielist.MainActivity
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [MainModule::class, MainComponent.MainActivityModule::class])
@ActivityScope
interface MainComponent : AndroidInjector<MainActivity> {

    @Module
    class MainActivityModule

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()
}
