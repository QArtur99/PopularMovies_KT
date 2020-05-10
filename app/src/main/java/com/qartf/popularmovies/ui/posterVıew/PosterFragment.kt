package com.qartf.popularmovies.ui.posterVıew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.qartf.popularmovies.R
import com.qartf.popularmovies.databinding.FragmentPosterBinding
import com.qartf.popularmovies.ui.movieDetail.MovieDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PosterFragment : Fragment() {

    private val movieDetailViewModel: MovieDetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentPosterBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_poster, container, false
        )
        binding.movieDetailViewModel = movieDetailViewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}