package com.example.radek.movielist

import com.example.radek.data.MovieListPagedDataProvider
import com.example.radek.jobexecutor.PagedDataProvider
import com.example.radek.model.MovieItem
import com.example.radek.model.SortOptionParameter
import com.example.radek.model.provider.MoviesPageProvider


class MovieListPagedDataProviderFactory(
        private val moviesPageProvider: MoviesPageProvider
) {

    fun create(sortBy: SortOptionParameter) : PagedDataProvider<MovieItem> {
        return MovieListPagedDataProvider(moviesPageProvider, sortBy)
    }
}
