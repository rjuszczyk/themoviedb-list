package com.example.radek.model

interface MoviesPage {
    val page:Int
    val totalPages:Int
    val pages: List<MovieItem>
}
