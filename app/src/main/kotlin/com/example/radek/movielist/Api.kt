package com.example.radek.movielist

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("/3/discover/movie?api_key=3af6e5afd969c81bd117f572fe387d96&language=en-US&include_adult=false&include_video=false")
    fun loadMoviesPage(@Query("page") page: Int, @Query("sort_by") sortBy:String): Call<NetModel>
}
