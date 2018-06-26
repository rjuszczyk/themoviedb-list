package com.example.radek.movielist.data.network

import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*

class OkHttpClientCreator {


    fun create(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor { chain ->
            val apiKey = chain.request().url().queryParameter("api_key")
            val request: Request
            request = if (apiKey == null) {
                val url = chain.request().url()
                        .newBuilder()
                        .addQueryParameter("api_key", Constants.API_KEY)
                        .addQueryParameter("language", getLanguageCode())
                        .build()

                chain.request().newBuilder()
                        .url(url)
                        .build()
            } else {
                chain.request()
            }
            chain.proceed(request)
        }
        return builder.build()
    }

    private fun getLanguageCode(): String {
        return Locale.getDefault().toString()
    }
}
