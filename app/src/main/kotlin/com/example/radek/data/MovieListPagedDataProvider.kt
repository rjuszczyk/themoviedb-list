package com.example.radek.data

import com.example.radek.jobexecutor.*
import com.example.radek.jobexecutor.response.InitialPagedResponse
import com.example.radek.jobexecutor.response.PagedResponse
import com.example.radek.model.MovieItem
import com.example.radek.model.MoviesPage
import com.example.radek.model.SortOptionParameter
import com.example.radek.model.provider.MoviesPageProvider

class MovieListPagedDataProvider(
        private val moviesPageProvider: MoviesPageProvider,
        private val sortBy:SortOptionParameter
) : PagedDataProvider<MovieItem> {
    private val jobCalls = ArrayList<MoviesPageProvider.Cancelable>()
    override fun dispose() {
        for (jobCall in jobCalls) {
            jobCall.cancel()
        }
        jobCalls.clear()
    }

    override fun provideInitialData(
            onLoaded: (InitialPagedResponse<MovieItem>) -> Unit,
            onFailed: (Throwable) -> Unit
    ) {
        val cancelable = moviesPageProvider.provideMoviePage(1, sortBy, object : MoviesPageProvider.Callback {
            override fun onSuccess(cancelable: MoviesPageProvider.Cancelable, moviesPage: MoviesPage) {
                jobCalls.remove(cancelable)
                onLoaded(InitialPagedResponse(moviesPage.totalPages, moviesPage.pages))
            }

            override fun onFailed(cancelable: MoviesPageProvider.Cancelable, throwable: Throwable) {
                jobCalls.remove(cancelable)
                onFailed(throwable)
            }
        })

        jobCalls.add(cancelable)
    }

    override fun providePageData(
            page: Int,
            onLoaded: (PagedResponse<MovieItem>) -> Unit,
            onFailed: (Throwable) -> Unit
    ) {
        val cancelable = moviesPageProvider.provideMoviePage(page, sortBy, object : MoviesPageProvider.Callback {
            override fun onSuccess(cancelable: MoviesPageProvider.Cancelable, moviesPage: MoviesPage) {
                jobCalls.remove(cancelable)
                onLoaded(PagedResponse(moviesPage.totalPages, moviesPage.page, moviesPage.pages))
            }

            override fun onFailed(cancelable: MoviesPageProvider.Cancelable, throwable: Throwable) {
                jobCalls.remove(cancelable)
                onFailed(throwable)
            }
        })

        jobCalls.add(cancelable)
    }
}
