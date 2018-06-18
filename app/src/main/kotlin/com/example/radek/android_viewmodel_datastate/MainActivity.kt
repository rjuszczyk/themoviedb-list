package com.example.radek.android_viewmodel_datastate

import android.arch.lifecycle.*
import android.arch.paging.DataSource
import android.arch.paging.PageKeyedDataSource
import android.arch.paging.PagedList
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel

    private val state: TextView by bindView(R.id.state)
    private val loadedString: TextView by bindView(R.id.loadedString)
    private val load: Button by bindView(R.id.load)
    private val recyclerView: RecyclerView by bindView(R.id.recyclerView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        log("onCreate")
        mainViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            @Suppress("unchecked_cast")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                    val state = savedInstanceState?.getInt("state") ?: MainViewDataState.NOT_STARTED

                    val viewModel = MainViewModel(MainStringRepository(), state)
                    if (state == MainViewDataState.LOADED || state == MainViewDataState.LOADING) {
                        viewModel.startLoading()
                    }

                    return viewModel as T
                }

                throw RuntimeException()
            }
        }).get(MainViewModel::class.java)

        mainViewModel.myString.observe(this, Observer {
            log("myString $it")
            loadedString.text = it
        })

        load.setOnClickListener {
            mainViewModel.startLoading()
        }

        mainViewModel.state.observe(this, Observer {
            it?.let {
                log("state ${it.state}")
                state.text = it.state.toString()
                when (it.state) {
                    MainViewDataState.LOADED, MainViewDataState.LOADING -> load.visibility = View.GONE
                    else -> load.visibility = View.VISIBLE
                }
            }
        })

        initRecycler()
    }

    lateinit var networkRepo: MainNetworkRepository
    lateinit var adapter: MyPagedListAdapter
    private fun initRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        networkRepo = MainNetworkRepository(
                { repositoryState -> adapter.repositoryState = repositoryState }
        )


        adapter = MyPagedListAdapter(
                object : MyPagedListAdapter.RetryListener {
                    override fun retryCalled() {
                        networkRepo.retryFailed()
                    }
                },
                object : DiffUtil.ItemCallback<NetResult>() {
                    override fun areItemsTheSame(oldItem: NetResult?, newItem: NetResult?): Boolean {
                        return oldItem == newItem
                    }

                    override fun areContentsTheSame(oldItem: NetResult?, newItem: NetResult?): Boolean {
                        return oldItem == newItem
                    }
                })

        val dataSource: DataSource<Int, NetResult>
        dataSource = object : PageKeyedDataSource<Int, NetResult>() {
            override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, NetResult>) {
                log("loadInitial")
                val key = 1
                networkRepo.loadPage(key, { netModel ->
                    val nextPage = if (key + 1 < netModel.total_pages) key + 1 else null
//                        callback.onResult(netModel.results!!, 0, netModel.total_results,  null, nextPage)
                    callback.onResult(netModel.results, null, nextPage)
                })
            }

            override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, NetResult>) {
                log("loadAfter")
                networkRepo.loadPage(params.key, { netModel ->
                    callback.onResult(netModel.results, params.key + 1)

                })
            }

            override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, NetResult>) {
                log("loadBefore")
                if (params.key < 1) {
                    callback.onResult(emptyList(), null)
                } else {
                    networkRepo.loadPage(params.key, { netModel ->
                        callback.onResult(netModel.results, params.key - 1)
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

        recyclerView.adapter = adapter
        adapter.submitList(pagedList)

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mainViewModel.state.value?.let {
            outState?.putInt("state", it.state)
        }
    }
}

