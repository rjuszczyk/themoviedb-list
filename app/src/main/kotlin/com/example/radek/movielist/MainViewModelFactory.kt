package com.example.radek.movielist

import com.example.radek.jobexecutor.MainNetworkRepository

class MainViewModelFactory (
        private val mainNetworkRepository: MainNetworkRepository<NetResult>
){
    fun create(): MainViewModel {
        return MainViewModel(mainNetworkRepository)
    }
}
