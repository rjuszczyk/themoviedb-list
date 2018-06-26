package com.example.radek.abs.adapter

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.radek.common.ChangeObservableProperty
import com.example.radek.jobexecutor.State
import kotlin.reflect.KProperty

abstract class AbsPagedListAdapter<T>(
        diffCallback: DiffUtil.ItemCallback<T>,
        private val retryListener: RetryListener
) : PagedListAdapter<T, AbsPagedListAdapter.MyHolder<T>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder<T> {
        return when (viewType) {
            0 -> {
                createItemViewHolder(parent)
            }
            1 -> {
                createFailedViewHolder(parent,retryListener)
            }
            else -> {
                createProgressViewHolder(parent)
            }
        }
    }

    protected abstract fun createProgressViewHolder(parent: ViewGroup): ProgressViewHolder

    protected abstract fun createFailedViewHolder(parent: ViewGroup,retryListener: RetryListener): FailedViewHolder<T>

    protected abstract fun createItemViewHolder(parent: ViewGroup): ItemViewHolder<T>

    private fun repositoryStateObserver () : ChangeObservableProperty<State> {
        return object : ChangeObservableProperty<State>(State.NotStarted) {
            override fun afterChange(property: KProperty<*>, oldValue: State, newValue: State) {
                var additionalRowDisplayed = false
                if (newValue == State.Loading || newValue is State.Failed) additionalRowDisplayed = true

                var additionalRowWasDisplayed = false
                if (oldValue == State.Loading || oldValue is State.Failed) additionalRowWasDisplayed = true

                if (additionalRowDisplayed && !additionalRowWasDisplayed) {
                    notifyItemInserted(super@AbsPagedListAdapter.getItemCount())
                } else if (!additionalRowDisplayed && additionalRowWasDisplayed) {
                    notifyItemChanged(super@AbsPagedListAdapter.getItemCount())
                } else if (additionalRowDisplayed && additionalRowWasDisplayed) {
                    notifyItemChanged(super@AbsPagedListAdapter.getItemCount())
                }
            }
        }
    }

    var repositoryState : State by repositoryStateObserver()

    override fun getItemCount(): Int {
        var additionalRowDisplayed = false
        if(repositoryState == State.Loading || repositoryState is State.Failed) additionalRowDisplayed = true
        return if (!additionalRowDisplayed) {
            super.getItemCount()
        } else {
            super.getItemCount() + 1
        }
    }

    override fun onBindViewHolder(holder: MyHolder<T>, position: Int) {
        if (holder is ItemViewHolder<T>) {
            val netResult = getItem(position)
            holder.bind(netResult)
        }
        if(holder is FailedViewHolder) {
            holder.bind((repositoryState as State.Failed).cause)
        }
    }

    override fun getItemViewType(position: Int): Int {
        var additionalRowDisplayed = false
        if(repositoryState == State.Loading || repositoryState is State.Failed) additionalRowDisplayed = true
//        return if (repositoryState != State.NotStarted && position == super.getItemCount()) {
        return if (additionalRowDisplayed && position == super.getItemCount()) {
            if (repositoryState is State.Failed) 1 else 2
        } else {
            0
        }
//        return 0
    }

    abstract class ItemViewHolder<T>(itemView: View) : MyHolder<T>(itemView) {
        abstract fun bind(item: T?)
    }

    abstract class FailedViewHolder<T>(itemView: View) : MyHolder<T>(itemView) {
        abstract fun bind(cause:Throwable)
    }

    inner class ProgressViewHolder(itemView: View) : MyHolder<T>(itemView)

    @Suppress("UNUSED")
    open class MyHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView)

    interface RetryListener {
        fun retryCalled()
    }
}
