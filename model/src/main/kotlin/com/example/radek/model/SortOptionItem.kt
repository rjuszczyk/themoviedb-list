package com.example.radek.movielist.data.model

import com.example.radek.model.SortOptionParameter

interface SortOptionItem{
    val parameter: SortOptionParameter
    val name: String
}