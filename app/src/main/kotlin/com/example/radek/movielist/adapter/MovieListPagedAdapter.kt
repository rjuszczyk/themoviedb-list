package com.example.radek.movielist.adapter

import android.annotation.SuppressLint
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.example.radek.R
import com.example.radek.abs.adapter.AbsPagedListAdapter
import com.example.radek.model.MovieItem

@SuppressLint("SetTextI18n")
class MovieListPagedAdapter(
        retryListener: RetryListener,
        diffCallback: DiffUtil.ItemCallback<MovieItem>
) : AbsPagedListAdapter<MovieItem>(retryListener, diffCallback) {

    override fun createProgressViewHolder(parent: ViewGroup): ProgressViewHolder {
        val tv = ProgressBar(parent.context)
        val height = (parent.context.resources.displayMetrics.density*50).toInt()
        tv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
        return ProgressViewHolder(tv)
    }

    override fun createFailedViewHolder(parent: ViewGroup,retryListener: RetryListener): FailedViewHolder<MovieItem> {
        val tv = Button(parent.context)
        val height = (parent.context.resources.displayMetrics.density*50).toInt()
        tv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
        return MyFailedViewHolder(tv, retryListener)
    }

    override fun createItemViewHolder(parent: ViewGroup): ItemViewHolder<MovieItem> {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)

        return MyItemViewHolder(view )
    }

    class MyItemViewHolder(itemView: View) : ItemViewHolder<MovieItem>(itemView) {
        private val title:TextView = itemView.findViewById(R.id.title)
        private val voteCount:TextView = itemView.findViewById(R.id.vote_count)
        private val voteAverage:TextView = itemView.findViewById(R.id.vote_average)
        private val releaseDate:TextView = itemView.findViewById(R.id.release_date)

        override fun bind(item: MovieItem?) {
            if (item != null) {
                title.text = item.title
                voteCount.text = item.voteCount
                voteAverage.text = item.voteAverage
                releaseDate.text = item.releaseDate
            } else {
                title.text = "loading"
                voteCount.text = ""
                voteAverage.text = ""
                releaseDate.text = ""
            }
        }
    }

    class MyFailedViewHolder(
            itemView: View,
            retryListener: RetryListener
    ) : FailedViewHolder<MovieItem>(itemView) {

        init {
            itemView.setOnClickListener({retryListener.retryCalled()})
        }

        override fun bind(cause: Throwable) {
            (itemView as TextView).text = cause.message
        }
    }
}
