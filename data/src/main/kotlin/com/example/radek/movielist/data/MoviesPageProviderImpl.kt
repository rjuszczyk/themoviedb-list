package com.example.radek.movielist.data

import com.example.radek.movielist.data.network.Api
import com.example.radek.movielist.data.network.model.MoviesResponse
import com.example.radek.model.MovieItem
import com.example.radek.model.MoviesPage
import com.example.radek.model.SortOptionParameter
import com.example.radek.model.provider.MoviesPageProvider
import retrofit2.Call
import retrofit2.Response

class MoviesPageProviderImpl(val api: Api): MoviesPageProvider {

    override fun provideMoviePage(page: Int, sortOption: SortOptionParameter, callback: MoviesPageProvider.Callback): MoviesPageProvider.Cancelable {
        val call = api.loadMoviesPage(page, sortOption.value)
        val cancelable = CancelableCall(call)
        call.enqueue(object : retrofit2.Callback<MoviesResponse> {
            override fun onFailure(call: Call<MoviesResponse>?, t: Throwable?) {
                callback.onFailed(cancelable, t!!)
            }

            override fun onResponse(call: Call<MoviesResponse>?, response: Response<MoviesResponse>?) {
                response?.body()?.let {
                    callback.onSuccess(cancelable, MoviesPageResult(it.page, it.total_pages, it.results))
                }?: run {
                    callback.onFailed(cancelable, Throwable("Invalid response"))
                }
            }
        })
        return cancelable
    }

    internal class CancelableCall(val call:Call<MoviesResponse> ) : MoviesPageProvider.Cancelable {
        override fun cancel() {
            call.cancel()
        }
    }

    internal class MoviesPageResult(override val page: Int, override val totalPages: Int, override val pages: List<MovieItem>) : MoviesPage

}
