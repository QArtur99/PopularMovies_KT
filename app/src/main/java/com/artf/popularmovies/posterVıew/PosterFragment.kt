package com.artf.popularmovies.posterVıew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.artf.popularmovies.R
import com.artf.popularmovies.database.MovieDatabase
import com.artf.popularmovies.databinding.FragmentPosterBinding
import com.artf.popularmovies.movieDetail.MovieDetailViewModel
import com.artf.popularmovies.movieDetail.MovieDetailViewModelFactory

class PosterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentPosterBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_poster, container, false
        )

        val application = requireNotNull(this.activity).application
        val movieDatabase = MovieDatabase.getInstance(application).movieDatabaseDao
        val viewModelFactory = MovieDetailViewModelFactory(movieDatabase)
        val movieDetailViewModel: MovieDetailViewModel =
            ViewModelProviders.of(activity!!, viewModelFactory).get(MovieDetailViewModel::class.java)

        binding.movieDetailViewModel = movieDetailViewModel
        binding.lifecycleOwner = this
        return binding.root
    }


}