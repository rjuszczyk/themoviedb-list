package com.example.radek.jobexecutor.response

abstract class AbsPagedResponse<T> {
    abstract val list: List<T>
}