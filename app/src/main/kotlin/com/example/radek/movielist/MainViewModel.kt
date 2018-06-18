package com.example.radek.movielist

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.DataSource
import android.arch.paging.PageKeyedDataSource
import android.arch.paging.PagedList
import android.os.Handler
import com.example.radek.jobexecutor.*
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor

class MainViewModel : ViewModel() {

    private val mainNetworkRepository: MainNetworkRepository<NetResult>
    internal var list = MutableLiveData<PagedList<NetResult>>()
    internal var repositoryState = MutableLiveData<State>()

    private val retrofit: Retrofit
    private val api: Api

    init {
        val builder = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .client(OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
        retrofit = builder.build()

        api = retrofit.create(Api::class.java)

        repositoryState.value = State.NotStarted
        mainNetworkRepository = MainNetworkRepository({ repositoryState.postValue(it) },
                object : PagedDataProvider<NetResult> {
                    override fun provideInitialData(
                            onLoaded: (InitialPagedResponse<NetResult>) -> Unit,
                            onFailed: (Throwable) -> Unit
                    ) {
                        api.loadMoviesPage(1).enqueue(object : Callback<NetModel> {
                            override fun onFailure(call: Call<NetModel>?, t: Throwable?) {
                                onFailed(t!!)
                            }

                            override fun onResponse(call: Call<NetModel>?, response: Response<NetModel>?) {
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
                        api.loadMoviesPage(page).enqueue(object : Callback<NetModel> {
                            override fun onFailure(call: Call<NetModel>?, t: Throwable?) {
                                onFailed(t!!)
                            }

                            override fun onResponse(call: Call<NetModel>?, response: Response<NetModel>?) {
                                val netModel = response!!.body()!!
                                onLoaded(PagedResponse(netModel.page, netModel.results))
                            }
                        })
                    }
                }
        )

        val dataSource: DataSource<Int, NetResult>
        dataSource = object : PageKeyedDataSource<Int, NetResult>() {
            override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, NetResult>) {
                log("loadInitial")
                val key = 1
                mainNetworkRepository.loadInitialPage {
                    val nextPage = if (key + 1 < it.totalPages) key + 1 else null
                    callback.onResult(it.list, null, nextPage)
                }
            }

            override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, NetResult>) {
                log("loadAfter")
                mainNetworkRepository.loadPage(params.key, { netModel ->
                    callback.onResult(netModel.list, netModel.page + 1)
                })
            }

            override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, NetResult>) {
                log("loadBefore")
                if (params.key < 1) {
                    callback.onResult(emptyList(), null)
                } else {
                    mainNetworkRepository.loadPage(params.key, { netModel ->
                        callback.onResult(netModel.list, netModel.page - 1)
                    })
                }
            }
        }

        val pagedList = PagedList.Builder(dataSource, 10)
                .setNotifyExecutor(object : Executor {
                    val handler = Handler()
                    override fun execute(command: Runnable?) {
                        handler.post(command)
                    }

                })
                .setFetchExecutor(object : Executor {
                    val handler = Handler()
                    override fun execute(command: Runnable?) {
                        handler.post(command)
                    }
                })
                .build()

        list.postValue(pagedList)
    }

    fun retry() {
        mainNetworkRepository.retryFailedJobs()
    }
}
