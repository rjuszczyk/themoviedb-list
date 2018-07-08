package com.example.radek.di.module

import android.os.Handler
import com.example.radek.di.scope.AppScope
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor

@Module
class ExecutorModule {

    @AppScope
    @Provides
    fun provideMainThreadExecutor(): Executor {
        val handler = Handler()
        return Executor { p0 -> handler.post(p0) }
    }
}