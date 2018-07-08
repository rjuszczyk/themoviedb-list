package com.example.radek.movielist.data

import com.example.radek.jobexecutor.*
import com.example.radek.jobexecutor.response.InitialPagedResponse
import com.example.radek.jobexecutor.response.FollowingPagedResponse
import com.example.radek.model.MovieItem
import com.example.radek.model.MoviesPage
import com.example.radek.model.SortOptionParameter
import com.example.radek.model.provider.MoviesPageDataProvider

class MovieListPagedDataProvider(
        private val moviesPageDataProvider: MoviesPageDataProvider,
        private val sortBy:SortOptionParameter
) : PagedDataProvider<MovieItem> {
    private val jobCalls = ArrayList<MoviesPageDataProvider.Cancelable>()
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
        val cancelable = moviesPageDataProvider.provideMoviePage(1, sortBy, object : MoviesPageDataProvider.Callback {
            override fun onSuccess(cancelable: MoviesPageDataProvider.Cancelable, moviesPage: MoviesPage) {
                jobCalls.remove(cancelable)
                onLoaded(InitialPagedResponse(moviesPage.totalPages, moviesPage.pages))
            }

            override fun onFailed(cancelable: MoviesPageDataProvider.Cancelable, throwable: Throwable) {
                jobCalls.remove(cancelable)
                onFailed(throwable)
            }
        })

        jobCalls.add(cancelable)
    }

    override fun providePageData(
            page: Int,
            onLoaded: (FollowingPagedResponse<MovieItem>) -> Unit,
            onFailed: (Throwable) -> Unit
    ) {
        val cancelable = moviesPageDataProvider.provideMoviePage(page, sortBy, object : MoviesPageDataProvider.Callback {
            override fun onSuccess(cancelable: MoviesPageDataProvider.Cancelable, moviesPage: MoviesPage) {
                jobCalls.remove(cancelable)
                onLoaded(FollowingPagedResponse(moviesPage.totalPages, moviesPage.page, moviesPage.pages))
            }

            override fun onFailed(cancelable: MoviesPageDataProvider.Cancelable, throwable: Throwable) {
                jobCalls.remove(cancelable)
                onFailed(throwable)
            }
        })

        jobCalls.add(cancelable)
    }
}
