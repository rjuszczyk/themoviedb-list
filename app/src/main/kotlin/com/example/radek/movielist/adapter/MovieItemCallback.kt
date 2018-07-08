package com.example.radek.movielist.adapter

import android.support.v7.util.DiffUtil
import com.example.radek.model.MovieItem

class MovieItemCallback : DiffUtil.ItemCallback<MovieItem>() {
    override fun areItemsTheSame(oldItem: MovieItem?, newItem: MovieItem?): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MovieItem?, newItem: MovieItem?): Boolean {
        return oldItem == newItem
    }
}
