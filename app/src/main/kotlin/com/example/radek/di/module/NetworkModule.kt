package com.example.radek.di.module

import com.example.radek.di.DiConstants
import com.example.radek.di.scope.AppScope
import com.example.radek.movielist.data.network.Api
import com.example.radek.movielist.data.network.OkHttpClientCreator
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class NetworkModule {

    @Provides
    @AppScope
    fun provideRetrofit(
            @Named(DiConstants.NAME_BASE_URL) baseUrl: String,
            okHttpClient: OkHttpClient,
            converterFactory: Converter.Factory
    ): Retrofit {
        val builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(converterFactory)
        return builder.build()
    }

    @Provides
    @AppScope
    fun provideConverterFactory(gson:Gson): Converter.Factory  {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @AppScope
    @Named(DiConstants.NAME_BASE_URL)
    fun provideBaseUrl(): String {
        return "https://api.themoviedb.org"
    }

    @Provides
    @AppScope
    fun provideOkHttpClientCreator(): OkHttpClientCreator {
        return OkHttpClientCreator()
    }

    @Provides
    @AppScope
    fun provideOkHttpClient(okHttpClientCreator: OkHttpClientCreator): OkHttpClient {
        return okHttpClientCreator.create()
    }

    @Provides
    @AppScope
    fun provideApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Provides
    @AppScope
    fun provideGson(): Gson {
        return Gson()
    }
}