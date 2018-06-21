package com.example.radek.movielist

import com.example.radek.jobexecutor.PagedDataProvider
import com.example.radek.movielist.model.MovieItem
import com.example.radek.network.Api
import com.example.radek.data.MovieListPagedDataProvider


class MovieListPagedDataProviderFactory(
        private val api: Api
) {

    fun create(sortBy:String) : PagedDataProvider<MovieItem> {
        return MovieListPagedDataProvider(api, sortBy)
    }
}
