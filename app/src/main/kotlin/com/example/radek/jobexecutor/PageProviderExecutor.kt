package com.example.radek.jobexecutor

import android.arch.lifecycle.MutableLiveData
import com.example.radek.jobexecutor.response.InitialPagedResponse
import com.example.radek.jobexecutor.response.PagedResponse

class PageProviderExecutor<T>(
        private var pagedDataProvider: PagedDataProvider<T>
) {
    private val jobsList = ArrayList<Job<*, T>>()
    internal var repositoryState = MutableLiveData<State>()

    fun changeDataProvider(pagedDataProvider: PagedDataProvider<T>) {
        this.pagedDataProvider.dispose()
        this.pagedDataProvider = pagedDataProvider
        jobsList.clear()
        repositoryState.postValue(State.NotStarted)
    }

    fun loadInitialPage(callback: (InitialPagedResponse<T>) -> Unit) {
        jobsList.add(InitialJob(callback))
        executeNextIfPossible()
    }

    fun loadPage(page: Int, callback: (PagedResponse<T>) -> Unit) {
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
            pagedDataProvider.provideInitialData(
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
            )
        } else if (job is PageJob<T>) {
            pagedDataProvider.providePageData(job.page,
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
                    })
        }
    }

    private fun updateCallbackAfterSuccess() {
        if (jobsList.isEmpty()) {
            repositoryState.postValue(State.Loaded)
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

        repositoryState.postValue(state)
    }
}




