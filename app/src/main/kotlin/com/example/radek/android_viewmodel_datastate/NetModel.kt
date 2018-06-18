package com.example.radek.android_viewmodel_datastate

import com.google.gson.annotations.SerializedName

data class NetModel(
        @SerializedName("page") val page: Int,
        @SerializedName("total_results") val total_results: Int,
        @SerializedName("total_pages") val total_pages: Int,
        @SerializedName("results") val results: List<NetResult>
)
