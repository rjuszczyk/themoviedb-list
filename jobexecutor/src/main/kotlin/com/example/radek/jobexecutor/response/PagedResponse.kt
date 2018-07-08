package com.example.radek.jobexecutor.response

abstract class PagedResponse<T> {
    abstract val list: List<T>
}