package com.example.radek.jobexecutor.response

data class PagedResponse<T>(
        val totalPages: Int,
        val page: Int,
        override val list: List<T>
) : AbsPagedResponse<T>()