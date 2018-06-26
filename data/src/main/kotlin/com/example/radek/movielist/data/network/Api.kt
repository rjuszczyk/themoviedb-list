package com.example.radek.movielist.data.network

import com.example.radek.movielist.data.network.model.MovieDetailsResponse
import com.example.radek.movielist.data.network.model.MoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("/3/discover/movie?include_adult=false&include_video=false")
    fun loadMoviesPage(@Query("page") page: Int, @Query("sort_by") sortBy:String): Call<MoviesResponse>

    @GET("/3/movie/{movie_id}")
    fun loadMovieDetails(@Path("movie_id") movieId: Int): Call<MovieDetailsResponse>

}
