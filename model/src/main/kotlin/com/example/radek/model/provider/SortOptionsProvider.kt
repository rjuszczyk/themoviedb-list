package com.example.radek.model.provider

import com.example.radek.movielist.data.model.SortOptionItem

interface SortOptionsProvider {
    fun provideSortOptionsList(callback: (List<SortOptionItem> ) -> Unit)



}
