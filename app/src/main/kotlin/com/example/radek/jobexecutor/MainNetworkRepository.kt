package com.example.radek.jobexecutor

class MainNetworkRepository<T>(
        private val repositoryStatusCallback: (State) -> Unit,
        private val pagedDataProvider: PagedDataProvider<T>
) {

    private val jobsList = ArrayList<Job<*, T>>()

    fun loadInitialPage(callback: (InitialPagedResponse<T>) -> Unit) {
        jobsList.add(InitialJob(callback))
        executeNextIfPossible()
    }

    fun loadPage(page: Int, callback: (PagedResponse<T>) -> Unit) {
        jobsList.add(PageJob(page, callback))
        executeNextIfPossible()
    }

    fun retryFailedJobs() {
        jobsList.filter { it.state == State.Failed }
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
        if (job is InitialJob<*>) {
            val j = job as InitialJob<T>
            pagedDataProvider.provideInitialData(
                    { response ->
                        j.state = State.Loaded
                        j.callback.invoke(response)

                        jobsList.remove(job)
                        executeNextIfPossible()
                    },
                    { _ ->
                        job.state = State.Failed
                        executeNextIfPossible()
                    }
            )
        } else if (job is PageJob<*>) {
            @Suppress("UNCHECKED_CAST")
            val j = job as PageJob<T>
            pagedDataProvider.providePageData(j.page,
                    { response ->
                        j.state = State.Loaded
                        j.callback.invoke(response)
                        jobsList.remove(job)
                        executeNextIfPossible()
                    },
                    { _ ->
                        job.state = State.Failed
                        executeNextIfPossible()
                    })
        }
    }

    private fun updateCallback() {
        var state: State = State.NotStarted
        if (jobsList.any { it.state == State.Loading }) {
            state = State.Loading
        }

        if (state != State.Loading) {
            //state = jobsList.firstOrNull({ it.state == State.Failed })?.state?:state
            if (jobsList.any { it.state == State.Failed }) {
                state = State.Failed
            }
        }

        repositoryStatusCallback(state)
    }
}




