package com.example.radek.movielist.data

import com.example.radek.jobexecutor.PagedDataProvider
import com.example.radek.model.MovieItem
import com.example.radek.model.SortOptionParameter
import com.example.radek.model.provider.MoviesPageDataProvider


class MovieListPagedDataProviderFactory(
        private val moviesPageDataProvider: MoviesPageDataProvider
) {

    fun create(sortBy: SortOptionParameter) : PagedDataProvider<MovieItem> {
        return MovieListPagedDataProvider(moviesPageDataProvider, sortBy)
    }
}
