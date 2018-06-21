package com.example.radek.movielist

import com.google.gson.annotations.SerializedName

data class NetResult(
        @SerializedName("id") val id: Int,
        @SerializedName("title") val title: String,
        @SerializedName("vote_count") val voteCount: String,
        @SerializedName("vote_average") val voteAverage: String,
        @SerializedName("release_date") val releaseDate: String
)
