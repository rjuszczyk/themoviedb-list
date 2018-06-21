package com.example.radek.movielist

data class SortOption(
        val parameter: String,
        val name: String
) {
    override fun toString() = name
}
