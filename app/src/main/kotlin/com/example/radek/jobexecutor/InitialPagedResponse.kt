package com.example.radek.jobexecutor

data class InitialPagedResponse<T>(
        val totalPages: Int,
        override val list: List<T>
) : AbsPagedResponse<T>()