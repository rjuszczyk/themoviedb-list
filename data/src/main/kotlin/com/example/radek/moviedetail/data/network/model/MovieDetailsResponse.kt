package com.example.radek.moviedetail.data.network.model

import com.example.radek.model.MovieDetailsItem
import com.google.gson.annotations.SerializedName

class MovieDetailsResponse(
        @SerializedName("id") override val id: Int,
        @SerializedName("title") override val title: String,
        @SerializedName("vote_count") override val voteCount: String,
        @SerializedName("vote_average") override val voteAverage: String,
        @SerializedName("release_date") override val releaseDate: String,
        @SerializedName("overview") override val overview: String) : MovieDetailsItem
