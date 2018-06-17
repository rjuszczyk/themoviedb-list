package com.example.radek.android_viewmodel_datastate

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("/3/discover/movie?api_key=3af6e5afd969c81bd117f572fe387d96&language=en-US&sort_by=release_date.desc&include_adult=false&include_video=false")
    fun test(@Query("page") page: Int): Call<NetModel>
}
