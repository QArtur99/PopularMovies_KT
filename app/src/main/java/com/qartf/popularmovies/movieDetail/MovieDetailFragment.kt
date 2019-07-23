package com.qartf.popularmovies.movieDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.qartf.popularmovies.R
import com.qartf.popularmovies.databinding.FragmentDetailBinding
import com.qartf.popularmovies.utility.extension.getVm

class MovieDetailFragment : Fragment() {

    private val movieDetailViewModel by lazy { getVm<MovieDetailViewModel>() }
    private lateinit var binding: FragmentDetailBinding
    private var onFabClickCounter: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.movieDetailViewModel = movieDetailViewModel
        binding.lifecycleOwner = this

        movieDetailViewModel.showReviews.observe(viewLifecycleOwner, Observer {
            it?.let {
                val reviewDialog = ReviewDialog()
                reviewDialog.show(requireFragmentManager(), ReviewDialog::class.simpleName)
                movieDetailViewModel.onShowReviews(null)
            }
        })

        movieDetailViewModel.showTrailers.observe(viewLifecycleOwner, Observer {
            it?.let {
                val reviewDialog = VideoDialog()
                reviewDialog.show(requireFragmentManager(), VideoDialog::class.simpleName)
                movieDetailViewModel.onShowTrailers(null)
            }
        })

        movieDetailViewModel.isFavorite.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                movieDetailViewModel.setFavorite(true)
                if (onFabClickCounter > 0) showSnackBar(1)
            } else {
                movieDetailViewModel.setFavorite(false)
                if (onFabClickCounter > 0) showSnackBar(2)
            }
        })

        movieDetailViewModel.listItem.observe(viewLifecycleOwner, Observer {
            it?.let {
                onFabClickCounter = 0
            }
        })

        movieDetailViewModel.fabButton.observe(viewLifecycleOwner, Observer {
            it?.let { value ->
                if (value) onFabClickCounter++
                movieDetailViewModel.setFabButton(null)
            }
        })

        return binding.root
    }

    private fun showSnackBar(snackBarId: Int) {
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            when (snackBarId) {
                1 -> getString(R.string.editor_insert_movie_successful)
                2 -> getString(R.string.editor_delete_product_successful)
                else -> getString(R.string.editor_delete_product_successful)
            },
            Snackbar.LENGTH_SHORT
        ).show()
    }
}