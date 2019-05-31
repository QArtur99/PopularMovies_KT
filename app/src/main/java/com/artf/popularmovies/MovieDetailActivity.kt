package com.artf.popularmovies

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.artf.popularmovies.databinding.ActivityMovieDetailBinding
import com.artf.popularmovies.domain.Movie
import com.artf.popularmovies.movieDetail.MovieDetailViewModel
import com.artf.popularmovies.movieDetail.MovieDetailViewModelFactory
import com.artf.popularmovies.utility.Constants.Companion.INTENT_LIST_ITEM
import com.google.android.material.appbar.AppBarLayout
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    private val movieDetailViewModel: MovieDetailViewModel by lazy {
        val viewModelFactory = MovieDetailViewModelFactory()
        ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel::class.java)
    }

    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.finish()
            return
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.lifecycleOwner = this
        binding.movieDetailViewModel = movieDetailViewModel
        binding.appBar.addOnOffsetChangedListener(this)


        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter<Movie>(Movie::class.java)
        val listItem: Movie = jsonAdapter.fromJson(intent.getStringExtra(INTENT_LIST_ITEM))!!

        movieDetailViewModel.setListItem(listItem)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val percentage = Math.abs(verticalOffset).toDouble() / collapsingToolbar.height
        if (percentage > 0.8) {
            movieDetailViewModel.onAppBarLayoutOpen(false)
        } else {
            movieDetailViewModel.onAppBarLayoutOpen(true)
        }
    }

}