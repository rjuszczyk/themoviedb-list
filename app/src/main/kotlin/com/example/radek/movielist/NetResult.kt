package com.example.radek.movielist

import com.google.gson.annotations.SerializedName

data class NetResult(
        @SerializedName("id") val id: Int,
        @SerializedName("title") var title: String
)
