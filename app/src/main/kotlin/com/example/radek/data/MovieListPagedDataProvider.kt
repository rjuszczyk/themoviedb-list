package com.example.radek.data

import com.example.radek.jobexecutor.*
import com.example.radek.jobexecutor.response.InitialPagedResponse
import com.example.radek.jobexecutor.response.PagedResponse
import com.example.radek.movielist.model.MovieItem
import com.example.radek.network.Api
import com.example.radek.network.model.MoviesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListPagedDataProvider(
        private val api: Api,
        private val sortBy:String = "release_date.desc"
) : PagedDataProvider<MovieItem> {
    private val jobCalls = ArrayList<Call<MoviesResponse>>()
    override fun dispose() {
        for (jobCall in jobCalls) {
            jobCall.cancel()
        }
        jobCalls.clear()
    }

    override fun provideInitialData(
            onLoaded: (InitialPagedResponse<MovieItem>) -> Unit,
            onFailed: (Throwable) -> Unit
    ) {
        val call = api.loadMoviesPage(1, sortBy)
        jobCalls.add(call)
        call.enqueue(object : Callback<MoviesResponse> {
            override fun onFailure(call: Call<MoviesResponse>?, t: Throwable?) {
                jobCalls.remove(call)
                onFailed(t!!)
            }

            override fun onResponse(call: Call<MoviesResponse>?, response: Response<MoviesResponse>?) {
                jobCalls.remove(call)
                val netModel = response!!.body()!!
                onLoaded(InitialPagedResponse(netModel.total_pages, netModel.results))
            }
        })
    }

    override fun providePageData(
            page: Int,
            onLoaded: (PagedResponse<MovieItem>) -> Unit,
            onFailed: (Throwable) -> Unit
    ) {
        val call = api.loadMoviesPage(page, sortBy)
        jobCalls.add(call)
        call.enqueue(object : Callback<MoviesResponse> {
            override fun onFailure(call: Call<MoviesResponse>?, t: Throwable?) {
                jobCalls.remove(call)
                onFailed(t!!)
            }

            override fun onResponse(call: Call<MoviesResponse>?, response: Response<MoviesResponse>?) {
                jobCalls.remove(call)
                val netModel = response!!.body()!!
                onLoaded(PagedResponse(netModel.total_pages, netModel.page, netModel.results))
            }
        })
    }
}
