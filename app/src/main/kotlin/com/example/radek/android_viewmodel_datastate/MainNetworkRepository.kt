package com.example.radek.android_viewmodel_datastate

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainNetworkRepository(val repositoryStatusCallback: NetworkRepositoryStateListener) {
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

    fun loadPage(page:Int, callback:JobLoaded) {
       jobsList.add(Job(page, callback))
       executeNextIfPossible()
    }

    internal fun executeNextIfPossible() {
        updateCallback()


        for (job in jobsList) {
            if(job.state == State.Loading) return
        }
        var jobToExecute:Job? = null

        for (job in jobsList) {
            if(job.state == State.NotStarted) {
                jobToExecute = job
                break
            }
        }

        jobToExecute?.let {
            execute(it)
        }
    }

    private fun updateCallback() {
        var failed:State = State.NotStarted
        if(jobsList.any { it.state == State.Loading }) {
            failed = State.Loading
        }

        if(failed != State.Loading) {
            //failed = jobsList.firstOrNull({ it.state == State.Failed })?.state?:failed
            if(jobsList.any { it.state == State.Failed }) {
                failed = State.Failed
            }
        }

        repositoryStatusCallback(failed)
    }

    fun retryFailed() {
        for (job in jobsList) {
            if(job.state == State.Failed) {
                job.state = State.NotStarted
            }
        }

        executeNextIfPossible()
    }

    internal fun execute(job:Job) {
        val call = api.test(job.page)
        job.state = State.Loading
        updateCallback()
        call.enqueue(object : Callback<NetModel> {
            override fun onFailure(call: Call<NetModel>?, t: Throwable?) {
                job.state = State.Failed
                executeNextIfPossible()
            }

            override fun onResponse(call: Call<NetModel>?, response: Response<NetModel>?) {
                job.state = State.Loaded
                job.callback(response!!.body()!!)

                jobsList.remove(job)
                executeNextIfPossible()
            }

        })
    }

    internal class Job(
            val page: Int,
            val callback: JobLoaded
    ) {
        var state:State = State.NotStarted
    }

    sealed class State {
        object NotStarted: State()
        object Loading: State()
        object Loaded: State()
        object Failed: State()
    }
}
internal typealias JobLoaded = (NetModel)->Unit
internal typealias NetworkRepositoryStateListener = (MainNetworkRepository.State)->Unit

