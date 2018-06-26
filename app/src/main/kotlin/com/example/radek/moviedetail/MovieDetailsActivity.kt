package com.example.radek.moviedetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.example.radek.R
import com.example.radek.common.bindView
import com.example.radek.model.MovieItem
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MovieDetailsActivity : DaggerAppCompatActivity() {
    @Inject lateinit var movieDetailsViewModelFactoryCreator: MovieDetailsViewModelFactoryCreator

    val movieItemId : Int by lazy {
        if(!intent.hasExtra(EXTRA_MOVIE_ITEM_ID)) throw IllegalArgumentException("Activity should be started using getStartIntent method only!")
        intent.getIntExtra(EXTRA_MOVIE_ITEM_ID, -1)
    }

    val movieDetailViewModel: MovieDetailsViewModel by lazy{
        ViewModelProviders.of(this@MovieDetailsActivity, movieDetailsViewModelFactoryCreator.create(movieItemId)).get(MovieDetailsViewModel::class.java)
    }

    private val progressBar: ProgressBar by bindView(R.id.progress)
    private val errorMessage: TextView by bindView(R.id.errorMessage)
    private val retry: Button by bindView(R.id.retry)
    private val description: TextView by bindView(R.id.description)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        retry.setOnClickListener({
            movieDetailViewModel.retryLoading()
        })

        movieDetailViewModel.state.observe(this, Observer {
            it!!.let{
                when (it) {
                    State.Loading -> {
                        progressBar.visibility = View.VISIBLE
                        errorMessage.visibility = View.GONE
                        retry.visibility = View.GONE
                        description.visibility = View.GONE
                    }
                    State.Loaded -> {
                        progressBar.visibility = View.GONE
                        errorMessage.visibility = View.GONE
                        retry.visibility = View.GONE
                        description.visibility = View.VISIBLE
                    }
                    State.NotStarted -> {
                        progressBar.visibility = View.GONE
                        errorMessage.visibility = View.GONE
                        retry.visibility = View.GONE
                        description.visibility = View.GONE
                    }
                    is State.Failed -> {
                        progressBar.visibility = View.GONE
                        errorMessage.visibility = View.VISIBLE
                        retry.visibility = View.VISIBLE
                        description.visibility = View.GONE

                        errorMessage.text = it.cause.localizedMessage
                    }
                }
            }
        })

        movieDetailViewModel.movieDetailsItem.observe(this, Observer {
            it!!.let {
                description.text = it.overview
            }
        })
    }


    companion object {
        val EXTRA_MOVIE_ITEM_ID = "EXTRA_MOVIE_ITEM_ID"
        fun getStartIntent(context: Context, movieItem:MovieItem): Intent {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra(EXTRA_MOVIE_ITEM_ID, movieItem.id)
            return intent
        }
    }
}
