package com.example.radek.jobexecutor

interface PagedDataProvider<T> {
    fun provideInitialData(onLoaded: (InitialPagedResponse<T>) -> Unit, onFailed: (Throwable) -> Unit)
    fun providePageData(page: Int, onLoaded: (PagedResponse<T>) -> Unit, onFailed: (Throwable) -> Unit)
    fun dispose()
}