package com.example.radek.jobexecutor

import com.example.radek.jobexecutor.response.InitialPagedResponse
import com.example.radek.jobexecutor.response.PagedResponse

interface PagedDataProvider<T> {
    fun provideInitialData(onLoaded: (InitialPagedResponse<T>) -> Unit, onFailed: (Throwable) -> Unit)
    fun providePageData(page: Int, onLoaded: (PagedResponse<T>) -> Unit, onFailed: (Throwable) -> Unit)
    fun dispose()
}