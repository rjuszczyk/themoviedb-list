package com.example.radek.jobexecutor

import com.example.radek.jobexecutor.response.AbsPagedResponse
import com.example.radek.jobexecutor.response.InitialPagedResponse
import com.example.radek.jobexecutor.response.PagedResponse

abstract class Job<E : AbsPagedResponse<T>, T> {
    abstract val callback: (E) -> Unit
    var state: State = State.NotStarted
}

class PageJob<T>(
        val page: Int,
        override val callback: (PagedResponse<T>) -> Unit
) : Job<PagedResponse<T>, T>()

class InitialJob<T>(
        override val callback: (InitialPagedResponse<T>) -> Unit
) : Job<InitialPagedResponse<T>, T>()