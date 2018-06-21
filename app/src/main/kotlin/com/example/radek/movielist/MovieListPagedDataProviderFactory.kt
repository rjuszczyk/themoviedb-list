package com.example.radek.movielist

import com.example.radek.jobexecutor.PagedDataProvider


class MovieListPagedDataProviderFactory(
        private val api:Api
) {

    fun create(sortBy:String) : PagedDataProvider<NetResult> {
        return MovieListPagedDataProvider(api,sortBy)
    }
}
