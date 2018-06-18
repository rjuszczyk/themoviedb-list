package com.example.radek.movielist

import android.annotation.SuppressLint
import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty
import com.example.radek.jobexecutor.State

@SuppressLint("SetTextI18n")
class MyPagedListAdapter(
        private val retryListener: RetryListener,
        diffCallback: DiffUtil.ItemCallback<NetResult>
) : PagedListAdapter<NetResult, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val height = (parent.context.resources.displayMetrics.density*50).toInt()
        return when (viewType) {
            0 -> {
                val tv = TextView(parent.context)
                tv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
                MyViewHolder(tv)
            }
            1 -> {
                val tv = Button(parent.context)
                tv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
                tv.text = "FAILED"
                tv.setOnClickListener({ retryListener.retryCalled() })
                FailedViewHolder(tv)
            }
            else -> {
                val tv = ProgressBar(parent.context)
                tv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
                LoadingViewHolder(tv)
            }
        }
    }

    var repositoryState : State by object:ObservableProperty<State>(State.NotStarted) {
        override fun afterChange(property: KProperty<*>, oldValue: State, newValue: State) {

            val rowInsertedOrRemoved = oldValue == State.NotStarted &&
                    newValue != State.NotStarted

            if (rowInsertedOrRemoved) {
                if (newValue != State.NotStarted) {
                    notifyItemInserted(super@MyPagedListAdapter.getItemCount())
                } else {
                    notifyItemRemoved(super@MyPagedListAdapter.getItemCount())
                }
            } else {
                notifyItemChanged(super@MyPagedListAdapter.getItemCount())
            }
        }
    }

    override fun getItemCount(): Int {
        if (repositoryState == State.NotStarted) {
            return super.getItemCount()
        } else {
            return super.getItemCount() + 1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            val netResult = getItem(position)
            if (netResult == null) {
                (holder.itemView as TextView).text = "LOADING"
            } else {
                (holder.itemView as TextView).text = netResult.title
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (repositoryState != State.NotStarted && position == super.getItemCount()) {
            if (repositoryState == State.Failed) 1 else 2
        } else {
            0
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class FailedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface RetryListener {
        fun retryCalled()
    }
}
