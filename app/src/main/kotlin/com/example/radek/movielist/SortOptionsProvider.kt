package com.example.radek.movielist

class SortOptionsProvider {
    fun provideSortOptionsList(callback: (List<SortOption> ) -> Unit) {
        callback.invoke(listOf(
                SortOption("popularity.asc", "popularity.asc"),
                SortOption("popularity.desc", "popularity.desc"),
                SortOption("release_date.asc", "release_date.asc"),
                SortOption("release_date.desc", "release_date.desc"),
                SortOption("revenue.asc", "revenue.asc"),
                SortOption("revenue.desc", "revenue.desc"),
                SortOption("primary_release_date.asc", "primary_release_date.asc"),
                SortOption("primary_release_date.desc", "primary_release_date.desc"),
                SortOption("original_title.asc", "original_title.asc"),
                SortOption("original_title.desc", "original_title.desc"),
                SortOption("vote_average.asc", "vote_average.asc"),
                SortOption("vote_average.desc", "vote_average.desc"),
                SortOption("vote_count.asc", "vote_count.asc"),
                SortOption("vote_count.desc", "vote_count.desc")
        ))
    }
}
