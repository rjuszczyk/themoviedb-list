package com.example.radek.jobexecutor

import com.example.radek.jobexecutor.response.InitialPagedResponse
import com.example.radek.jobexecutor.response.FollowingPagedResponse

interface PagedDataProvider<T> {
    fun provideInitialData(onLoaded: (InitialPagedResponse<T>) -> Unit, onFailed: (Throwable) -> Unit)
    fun providePageData(page: Int, onLoaded: (FollowingPagedResponse<T>) -> Unit, onFailed: (Throwable) -> Unit)
    fun dispose()
}