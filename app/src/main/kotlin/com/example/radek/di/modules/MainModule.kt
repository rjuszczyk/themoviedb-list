package com.example.radek.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.example.radek.di.scope.ActivityScope
import com.example.radek.jobexecutor.InitialPagedResponse
import com.example.radek.jobexecutor.MainNetworkRepository
import com.example.radek.jobexecutor.PagedDataProvider
import com.example.radek.jobexecutor.PagedResponse
import com.example.radek.movielist.*
import dagger.Module
import dagger.Provides
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Module
class MainModule {
    @Provides
    @ActivityScope
    fun provideMainNetworkRepository(api: Api): MainNetworkRepository<NetResult> {
        return MainNetworkRepository(
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
    }

    @Provides
    @ActivityScope
    fun provideMainViewModelFactory(mainNetworkRepository:MainNetworkRepository<NetResult> ) : MainViewModelFactory {
        return MainViewModelFactory(mainNetworkRepository)
    }

    @Suppress("UNCHECKED_CAST")
    @Provides
    @ActivityScope
    fun provideMainViewModel(mainActivity: MainActivity, mainViewModelFactory:MainViewModelFactory) : MainViewModel {
        return ViewModelProviders.of(mainActivity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return mainViewModelFactory.create() as T
            }
        }).get(MainViewModel::class.java)
    }
}