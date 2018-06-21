package com.example.radek.movielist

import com.example.radek.jobexecutor.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListPagedDataProvider(
        private val api:Api,
        private val sortBy:String = "release_date.desc"
) : PagedDataProvider<NetResult> {
    private val jobCalls = ArrayList<Call<NetModel>>()
    override fun dispose() {
        for (jobCall in jobCalls) {
            jobCall.cancel()
        }
        jobCalls.clear()
    }

    override fun provideInitialData(
            onLoaded: (InitialPagedResponse<NetResult>) -> Unit,
            onFailed: (Throwable) -> Unit
    ) {
        val call = api.loadMoviesPage(1, sortBy)
        jobCalls.add(call)
        call.enqueue(object : Callback<NetModel> {
            override fun onFailure(call: Call<NetModel>?, t: Throwable?) {
                jobCalls.remove(call)
                onFailed(t!!)
            }

            override fun onResponse(call: Call<NetModel>?, response: Response<NetModel>?) {
                jobCalls.remove(call)
                val netModel = response!!.body()!!
                onLoaded(InitialPagedResponse(netModel.total_pages, netModel.results))
            }
        })
    }

    override fun providePageData(
            page: Int,
            onLoaded: (PagedResponse<NetResult>) -> Unit,
            onFailed: (Throwable) -> Unit
    ) {
        val call = api.loadMoviesPage(page, sortBy)
        jobCalls.add(call)
        call.enqueue(object : Callback<NetModel> {
            override fun onFailure(call: Call<NetModel>?, t: Throwable?) {
                jobCalls.remove(call)
                onFailed(t!!)
            }

            override fun onResponse(call: Call<NetModel>?, response: Response<NetModel>?) {
                jobCalls.remove(call)
                val netModel = response!!.body()!!
                onLoaded(PagedResponse(netModel.total_pages, netModel.page, netModel.results))
            }
        })
    }
}
