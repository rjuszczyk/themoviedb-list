package com.example.radek.movielist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import com.example.radek.R
import com.example.radek.abs.adapter.AbsPagedListAdapter
import com.example.radek.common.bindView
import com.example.radek.model.MovieItem
import com.example.radek.model.SortOptionParameter
import com.example.radek.moviedetail.MovieDetailsActivity
import com.example.radek.movielist.adapter.MovieListPagedAdapter
import com.example.radek.movielist.data.model.SortOptionItem
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MovieListActivity : DaggerAppCompatActivity() {
    @Inject lateinit var movieListViewModelFactory:MovieListViewModelFactory
    val movieListViewModel: MovieListViewModel by lazy{
        ViewModelProviders.of(this@MovieListActivity, movieListViewModelFactory).get(MovieListViewModel::class.java)
    }

    private lateinit var pagedAdapter: MovieListPagedAdapter
    private val recyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val changeSortBy: Spinner by bindView(R.id.changeSortBy)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSpinner()
        initRecycler()

        movieListViewModel.list.observe(this, Observer {
            pagedAdapter.submitList(it)
        })
        movieListViewModel.repositoryState.observe(this, Observer {
            it?.let { pagedAdapter.repositoryState = it }
        })
    }

    private fun initSpinner() {

        val adapter = ArrayAdapter<SortOptionItem>(this, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        changeSortBy.adapter = adapter
        movieListViewModel.sortOptions.observe(this, Observer<List<SortOptionItem>> {
            adapter.clear()
            it?.let {
                val printableList = it.map { object : SortOptionItem {
                    override val parameter: SortOptionParameter
                        get() = it.parameter
                    override val name: String
                        get() = it.name

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
                movieListViewModel.sortOption = sortOption.parameter
            }
        }
    }

    private fun initRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        pagedAdapter = MovieListPagedAdapter(
                object : DiffUtil.ItemCallback<MovieItem>() {
                    override fun areItemsTheSame(oldItem: MovieItem?, newItem: MovieItem?): Boolean {
                        return oldItem == newItem
                    }

                    override fun areContentsTheSame(oldItem: MovieItem?, newItem: MovieItem?): Boolean {
                        return oldItem == newItem
                    }
                },
                object : AbsPagedListAdapter.RetryListener {
                    override fun retryCalled() {
                        movieListViewModel.retry()
                    }
                },
                object : MovieListPagedAdapter.MovieItemClickedListener {
                    override fun onItemClicked(movieItem: MovieItem) {
                        startActivity(MovieDetailsActivity.getStartIntent(this@MovieListActivity, movieItem))
                    }

                }
        )

        recyclerView.adapter = pagedAdapter
    }
}

