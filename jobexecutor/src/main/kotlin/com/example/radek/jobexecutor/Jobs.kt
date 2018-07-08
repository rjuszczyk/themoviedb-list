package com.example.radek.jobexecutor

import com.example.radek.jobexecutor.response.PagedResponse
import com.example.radek.jobexecutor.response.InitialPagedResponse
import com.example.radek.jobexecutor.response.FollowingPagedResponse

abstract class Job<E : PagedResponse<T>, T> {
    abstract val callback: (E) -> Unit
    var state: State = State.NotStarted
}

class PageJob<T>(
        val page: Int,
        override val callback: (FollowingPagedResponse<T>) -> Unit
) : Job<FollowingPagedResponse<T>, T>()

class InitialJob<T>(
        override val callback: (InitialPagedResponse<T>) -> Unit
) : Job<InitialPagedResponse<T>, T>()