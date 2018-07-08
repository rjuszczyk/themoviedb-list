package com.example.radek.jobexecutor.response

data class FollowingPagedResponse<T>(
        val totalPages: Int,
        val page: Int,
        override val list: List<T>
) : PagedResponse<T>()