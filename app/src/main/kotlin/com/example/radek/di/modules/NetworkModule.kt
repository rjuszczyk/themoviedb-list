package com.example.radek.di.modules

import com.example.radek.di.DiConstants
import com.example.radek.di.scope.AppScope
import com.example.radek.movielist.Api
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class NetworkModule {

    @Provides
    @AppScope
    fun provideRetrofit(@Named(DiConstants.NAME_BASE_URL) baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        val builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
        return builder.build()
    }

    @Named(DiConstants.NAME_BASE_URL)
    @Provides
    @AppScope
    fun provideBaseUrl(): String {
        return "https://api.themoviedb.org"
    }

    @Provides
    @AppScope
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

    @Provides
    @AppScope
    fun provideApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }
}
