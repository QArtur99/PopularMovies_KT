package com.qartf.popularmovies.posterVıew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.qartf.popularmovies.R
import com.qartf.popularmovies.databinding.FragmentPosterBinding
import com.qartf.popularmovies.movieDetail.MovieDetailViewModel
import com.qartf.popularmovies.movieDetail.MovieDetailViewModelFactory
import com.qartf.popularmovies.utility.ServiceLocator

class PosterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentPosterBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_poster, container, false
        )

        val application = requireNotNull(this.activity).application
        val repository = ServiceLocator.instance(application).getRepository()
        val viewModelFactory = MovieDetailViewModelFactory(repository)
        val movieDetailViewModel: MovieDetailViewModel =
            ViewModelProviders.of(activity!!, viewModelFactory).get(MovieDetailViewModel::class.java)

        binding.movieDetailViewModel = movieDetailViewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}