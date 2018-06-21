package com.example.radek.movielist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import com.example.radek.R
import com.example.radek.abs.adapter.AbsPagedListAdapter
import com.example.radek.movielist.adapter.MyPagedListAdapter
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import android.widget.AdapterView
import com.example.radek.common.bindView
import com.example.radek.movielist.model.MovieItem
import com.example.radek.movielist.model.SortOptionItem


class MainActivity : DaggerAppCompatActivity() {
    @Inject lateinit var mainViewModelFactory:MainViewModelFactory
    val mainViewModel: MainViewModel by lazy{
        ViewModelProviders.of(this@MainActivity, mainViewModelFactory).get(MainViewModel::class.java)
    }

    private lateinit var adapter: MyPagedListAdapter
    private val recyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val changeSortBy: Spinner by bindView(R.id.changeSortBy)

    override fun onCreate(savedInstanceState: Bundle?) {
      //  AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSpinner()
        initRecycler()

        mainViewModel.list.observe(this, Observer {
            adapter.submitList(it)
        })
        mainViewModel.repositoryState.observe(this, Observer {
            it?.let { adapter.repositoryState = it }
        })
//        changeSortBy.setOnClickListener {
//            lastB = !lastB
//            mainViewModel.updateSearch(lastB)
//        }
    }

    private fun initSpinner() {

        val adapter = ArrayAdapter<SortOptionItem>(this, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        changeSortBy.adapter = adapter
        mainViewModel.sortOptions.observe(this, Observer<List<SortOptionItem>> {
            adapter.clear()
            it?.let {
                val printableList = it.map { object : SortOptionItem {
                    override val parameter: String
                        get() = it.name
                    override val name: String
                        get() = it.parameter

                    override fun toString(): String {
                        return name
                    }
                } }
                adapter.addAll(printableList)
            }
        })

        changeSortBy.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val sortOption = adapter.getItem(p2)
                mainViewModel.sortOption = sortOption.parameter
            }
        }
    }

    private fun initRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        adapter = MyPagedListAdapter(
                object : AbsPagedListAdapter.RetryListener {
                    override fun retryCalled() {
                        mainViewModel.retry()
                    }
                },
                object : DiffUtil.ItemCallback<MovieItem>() {
                    override fun areItemsTheSame(oldItem: MovieItem?, newItem: MovieItem?): Boolean {
                        return oldItem == newItem
                    }

                    override fun areContentsTheSame(oldItem: MovieItem?, newItem: MovieItem?): Boolean {
                        return oldItem == newItem
                    }
                })

        recyclerView.adapter = adapter
    }
}

