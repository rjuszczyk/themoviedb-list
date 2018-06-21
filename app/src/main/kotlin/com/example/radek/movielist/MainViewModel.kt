package com.example.radek.movielist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.DataSource
import android.arch.paging.PageKeyedDataSource
import android.arch.paging.PagedList
import com.example.radek.ChangeObservableProperty
import com.example.radek.jobexecutor.MainNetworkRepository
import com.example.radek.jobexecutor.State
import java.util.concurrent.Executor
import kotlin.reflect.KProperty

class MainViewModel(
        sortOptionsProvider : SortOptionsProvider,
        private val movieListPagedDataProviderFactory: MovieListPagedDataProviderFactory,
        private val mainNetworkRepository: MainNetworkRepository<NetResult>,
        private val mainThreadExecutor: Executor
) : ViewModel() {
    val sortOptions = MutableLiveData<List<SortOption>>()
    val sortBy = MutableLiveData<String>()
    val list:LiveData<PagedList<NetResult>>// = MutableLiveData<PagedList<NetResult>>()
    val repositoryState : MutableLiveData<State> = mainNetworkRepository.repositoryState

    val initialValue = "release_date.desc"

    init {
        sortOptionsProvider.provideSortOptionsList { sortOptions.postValue(it) }
        repositoryState.value = State.NotStarted
        sortBy.value = initialValue

        list = Transformations.map(sortBy) { input -> preparePagedList(mainThreadExecutor, input) }
    }


    private fun preparePagedList(mainThreadExecutor: Executor, sortBy: String): PagedList<NetResult> {

        val dataSource: DataSource<Int, NetResult>

        mainNetworkRepository.changeDataProvider(movieListPagedDataProviderFactory.create(sortBy))

        dataSource = object : PageKeyedDataSource<Int, NetResult>() {
            override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, NetResult>) {
                val key = 1
                mainNetworkRepository.loadInitialPage {
                    val nextPage = if (key + 1 < it.totalPages) key + 1 else null
                    callback.onResult(it.list, null, nextPage)
                }
            }

            override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, NetResult>) {
                mainNetworkRepository.loadPage(params.key) {
//                    val nextPage = if (params.key + 1 < it.totalPages) params.key + 1 else null
                    val nextPage = if (params.key + 1 <= 4) params.key + 1 else null
                    callback.onResult(it.list, nextPage)
                }
            }

            override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, NetResult>) {
                if (params.key < 1) {
                    callback.onResult(emptyList(), null)
                } else {
                    mainNetworkRepository.loadPage(params.key) { netModel ->
                        callback.onResult(netModel.list, netModel.page - 1)
                    }
                }
            }
        }

        return PagedList.Builder(dataSource, 10)
                .setNotifyExecutor(mainThreadExecutor)
                .setFetchExecutor(mainThreadExecutor)
                .build()
    }

    fun retry() {
        mainNetworkRepository.retryFailedJobs()
    }

    var sortOption:String by sortObservable()
    private fun sortObservable () : ChangeObservableProperty<String> {
        return object: ChangeObservableProperty<String>(initialValue){
            override fun afterChange(property: KProperty<*>, oldValue: String, newValue: String) {
                sortBy.postValue(newValue)
            }
        }
    }
}
