package com.example.radek.movielist.data

import com.example.radek.movielist.data.model.SortOption
import com.example.radek.movielist.data.model.SortOptionItem
import com.example.radek.model.SortOptionParameter
import com.example.radek.model.provider.SortOptionsProvider

class SortOptionsProviderImpl : SortOptionsProvider {
    override fun provideSortOptionsList(callback: (List<SortOptionItem> ) -> Unit) {
        callback.invoke(listOf(
                SortOption(SortOptionParameter.POPULARITY_ASC, "popularity.asc"),
                SortOption(SortOptionParameter.POPULARITY_DESC, "popularity.desc"),
                SortOption(SortOptionParameter.RELEASE_DATE_ASC, "release_date.asc"),
                SortOption(SortOptionParameter.RELEASE_DATE_DESC, "release_date.desc"),
                SortOption(SortOptionParameter.REVENUE_ASC, "revenue.asc"),
                SortOption(SortOptionParameter.REVENUE_DESC, "revenue.desc"),
                SortOption(SortOptionParameter.PRIMARY_RELEASE_DATE_ASC, "primary_release_date.asc"),
                SortOption(SortOptionParameter.PRIMARY_RELEASE_DATE_DESC, "primary_release_date.desc"),
                SortOption(SortOptionParameter.ORIGINAL_TITLE_ASC, "original_title.asc"),
                SortOption(SortOptionParameter.ORIGINAL_TITLE_DESC, "original_title.desc"),
                SortOption(SortOptionParameter.VOTE_AVERAGE_ASC, "vote_average.asc"),
                SortOption(SortOptionParameter.VOTE_AVERAGE_DESC, "vote_average.desc"),
                SortOption(SortOptionParameter.VOTE_COUNT_ASC, "vote_count.asc"),
                SortOption(SortOptionParameter.VOTE_COUNT_DESC, "vote_count.desc")
        ))
    }
}
