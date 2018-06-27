package com.example.radek.moviedetail.data

import com.example.radek.model.provider.MovieDetailsProvider
import com.example.radek.movielist.data.network.Api
import com.example.radek.moviedetail.data.network.model.MovieDetailsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsProviderImpl(val api: Api): MovieDetailsProvider {
    override fun provideMovieDetails(movieItemId: Int, callback: MovieDetailsProvider.Callback) {
        api.loadMovieDetails(movieItemId).enqueue(object:Callback<MovieDetailsResponse>{
            override fun onResponse(call: Call<MovieDetailsResponse>, response: Response<MovieDetailsResponse>) {
                response.body()?.let {
                    callback.onSuccess(it)
                }?: run {
                    callback.onFailed(Throwable("Invalid response"))
                }
            }

            override fun onFailure(call: Call<MovieDetailsResponse>, t: Throwable) {
                callback.onFailed(t)
            }

        })
    }
}
