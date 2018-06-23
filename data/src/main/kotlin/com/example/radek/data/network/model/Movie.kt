package com.example.radek.data.network.model

import com.example.radek.model.MovieItem
import com.google.gson.annotations.SerializedName

data class Movie(
        @SerializedName("id") override val id: Int,
        @SerializedName("title") override val title: String,
        @SerializedName("vote_count") override val voteCount: String,
        @SerializedName("vote_average") override val voteAverage: String,
        @SerializedName("release_date") override val releaseDate: String) : MovieItem
