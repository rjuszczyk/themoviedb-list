package com.example.radek.jobexecutor

import com.example.radek.jobexecutor.response.InitialPagedResponse
import com.example.radek.jobexecutor.response.FollowingPagedResponse
import java.util.*

/**
 * Note that attachDataProvider has to be called before loading any page, otherwise
 * ProviderNotAttachedException will be thrown
 */
class PageProviderExecutor<T> {
    private var pagedDataProvider: PagedDataProvider<T>? = null
    private val jobsList = ArrayList<Job<*, T>>()
    lateinit var repositoryState: (State) -> Unit

    fun attachDataProvider(pagedDataProvider: PagedDataProvider<T>) {
        this.pagedDataProvider?.dispose()
        this.pagedDataProvider = pagedDataProvider
        jobsList.clear()
        repositoryState(State.NotStarted)
    }

    fun loadInitialPage(callback: (InitialPagedResponse<T>) -> Unit) {
        jobsList.add(InitialJob(callback))
        executeNextIfPossible()
    }

    fun loadPage(page: Int, callback: (FollowingPagedResponse<T>) -> Unit) {
        jobsList.add(PageJob(page, callback))
        executeNextIfPossible()
    }

    fun retryFailedJobs() {
        jobsList.filter { it.state is State.Failed }
                .forEach { it.state = State.NotStarted }
        executeNextIfPossible()
    }

    private fun executeNextIfPossible() {
        if (jobsList.any { it.state == State.Loading }) return

        jobsList.firstOrNull { job -> job.state == State.NotStarted }?.let {
            execute(it)
        }
    }

    private fun execute(job: Job<*, T>) {
        job.state = State.Loading
        updateCallback()
        if (job is InitialJob<T>) {
            pagedDataProvider?.provideInitialData(
                    { response ->
                        job.state = State.Loaded
                        job.callback.invoke(response)

                        jobsList.remove(job)
                        updateCallbackAfterSuccess()
                        executeNextIfPossible()
                    },
                    { throwable ->
                        job.state = State.Failed(throwable)
                        updateCallback()
                        executeNextIfPossible()
                    }
            ) ?: run {
                throw ProviderNotAttachedException()
            }
        } else if (job is PageJob<T>) {
            pagedDataProvider?.providePageData(job.page,
                    { response ->
                        job.state = State.Loaded
                        job.callback.invoke(response)
                        jobsList.remove(job)
                        updateCallbackAfterSuccess()
                    },
                    { throwable ->
                        job.state = State.Failed(throwable)
                        updateCallback()
                        executeNextIfPossible()
                    }
            ) ?: run {
                throw ProviderNotAttachedException()
            }
        }
    }

    private fun updateCallbackAfterSuccess() {
        if (jobsList.isEmpty()) {
            repositoryState(State.Loaded)
        } else {
            updateCallback()
            executeNextIfPossible()
        }
    }

    private fun updateCallback() {
        var state: State = State.NotStarted
        if (jobsList.any { it.state == State.Loading || it.state == State.NotStarted }) {
            state = State.Loading
        }

        if (state != State.Loading) {
            jobsList.firstOrNull { it.state is State.Failed }?.let {
                state = it.state
            }
        }

        repositoryState(state)
    }

    class ProviderNotAttachedException : IllegalStateException("You have to attach provider before requesting any page!")
}




