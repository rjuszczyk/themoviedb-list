package com.example.radek.android_viewmodel_datastate

class MainViewDataState(val state: Int) {
    companion object {
        val NOT_STARTED = -1
        val LOADING = 0
        val LOADED = 1
        val FAILED = 2
    }
}
