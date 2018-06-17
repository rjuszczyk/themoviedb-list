package com.example.radek.android_viewmodel_datastate

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class MyPagedListAdapter (
        val retryListener: RetryListener,
        diffCallback: DiffUtil.ItemCallback<NetResult>) : PagedListAdapter<NetResult,  RecyclerView.ViewHolder>(diffCallback) {

    var isFailed = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder {
        if(viewType == 0) {
            return MyViewHolder(TextView(parent.context))
        } else {
            val tv = Button(parent.context)
            tv.setOnClickListener(View.OnClickListener { retryListener.retryCalled() })
            return FailedViewHolder(tv )
        }
    }

    fun updateFailed(isFailed:Boolean) {
        if(this.isFailed != isFailed) {
            this.isFailed = isFailed
            if(isFailed) {
                notifyItemInserted(super.getItemCount())
            } else {
                notifyItemRemoved(super.getItemCount())
            }
        }
    }

    override fun getItemCount(): Int {
        if(!isFailed) {
            return super.getItemCount()
        } else {
            return super.getItemCount()+1
        }
    }

    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        if(holder is MyViewHolder) {
            val netResult = getItem(position)
            if (netResult == null) {
                (holder.itemView as TextView).text = "LOADING"
            } else {
                val title = netResult.title!!
                (holder.itemView as TextView).text = title
            }
        } else {
            (holder.itemView as TextView).text = "FAILED"
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(isFailed && position == super.getItemCount()) {
            return 1
        } else {
            return 0
        }

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class FailedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface RetryListener {
        fun retryCalled()
    }
}
