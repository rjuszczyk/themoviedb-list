package com.example.radek.model

sealed class SortOptionParameter( val value: String) {

    object POPULARITY_ASC : SortOptionParameter("popularity.asc")
    object POPULARITY_DESC : SortOptionParameter("popularity.desc")
    object RELEASE_DATE_ASC : SortOptionParameter("release_date.asc")
    object RELEASE_DATE_DESC : SortOptionParameter("release_date.desc")
    object REVENUE_ASC : SortOptionParameter("revenue.asc")
    object REVENUE_DESC : SortOptionParameter("revenue.desc")
    object PRIMARY_RELEASE_DATE_ASC : SortOptionParameter("primary_release_date.asc")
    object PRIMARY_RELEASE_DATE_DESC : SortOptionParameter("primary_release_date.desc")
    object ORIGINAL_TITLE_ASC : SortOptionParameter("original_title.asc")
    object ORIGINAL_TITLE_DESC : SortOptionParameter("original_title.desc")
    object VOTE_AVERAGE_ASC : SortOptionParameter("vote_average.asc")
    object VOTE_AVERAGE_DESC : SortOptionParameter("vote_average.desc")
    object VOTE_COUNT_ASC : SortOptionParameter("vote_count.asc")
    object VOTE_COUNT_DESC : SortOptionParameter("vote_count.desc")
}
