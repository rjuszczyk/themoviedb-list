package com.example.radek.jobexecutor

data class PagedResponse<T>(
        val page: Int,
        override val list: List<T>
) : AbsPagedResponse<T>()