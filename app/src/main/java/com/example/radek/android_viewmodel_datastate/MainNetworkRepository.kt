package com.example.radek.android_viewmodel_datastate

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainNetworkRepository(val failureCallback: FailureCallback) {
    val retrofit : Retrofit
    val api : Api
    init {
        val builder = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .client(OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
        retrofit = builder.build()

        api = retrofit.create(Api::class.java)
    }

    internal val jobsList = ArrayList<Job>()

    fun loadPage(page:Int, callback:PageCallback) {
       jobsList.add(Job(page, callback))
        executeNextIfPossible()
    }

    internal fun executeNextIfPossible() {
        var failed = false
        for (job in jobsList) {
            if(job.state == 3) {
                failed = true
                break
            }
        }
        failureCallback.onFailed(failed)


        for (job in jobsList) {
            if(job.state == 1) return
        }
        var jobToExecute:Job? = null

        for (job in jobsList) {
            if(job.state == 0) {
                jobToExecute = job
                break
            }
        }

        jobToExecute?.let {
            execute(it)
        }
    }

    fun retryFailed() {
        for (job in jobsList) {
            if(job.state == 3) {
                job.state = 0
            }
        }

        executeNextIfPossible()
    }

    internal fun execute(job:Job) {
        val call = api.test(job.page)
        job.state = 1
        call.enqueue(object : Callback<NetModel> {
            override fun onFailure(call: Call<NetModel>?, t: Throwable?) {
                job.state = 3
                executeNextIfPossible()
            }

            override fun onResponse(call: Call<NetModel>?, response: Response<NetModel>?) {
                job.state = 2
                job.callback.onLoaded(response!!.body()!!)

                jobsList.remove(job)
                executeNextIfPossible()
            }

        })
    }

    internal class Job(
            val page: Int,
            val callback: PageCallback
    ) {
        var state:Int = 0
    }

    interface PageCallback {
        fun onLoaded(netModel:NetModel)
    }

    interface FailureCallback {
        fun onFailed(isFailed:Boolean)
    }
}
