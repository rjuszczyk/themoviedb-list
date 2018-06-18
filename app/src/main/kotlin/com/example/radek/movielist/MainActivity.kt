package com.example.radek.movielist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: MyPagedListAdapter
    private val recyclerView: RecyclerView by bindView(R.id.recyclerView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        log("onCreate")

        initRecycler()

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        mainViewModel.list.observe(this, Observer {
            adapter.submitList(it)
        })
        mainViewModel.repositoryState.observe(this, Observer {
            it?.let { adapter.repositoryState = it }
        })
    }


    private fun initRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        adapter = MyPagedListAdapter(
                object : MyPagedListAdapter.RetryListener {
                    override fun retryCalled() {
                        mainViewModel.retry()
                    }
                },
                object : DiffUtil.ItemCallback<NetResult>() {
                    override fun areItemsTheSame(oldItem: NetResult?, newItem: NetResult?): Boolean {
                        return oldItem == newItem
                    }

                    override fun areContentsTheSame(oldItem: NetResult?, newItem: NetResult?): Boolean {
                        return oldItem == newItem
                    }
                })

        recyclerView.adapter = adapter
    }
}

