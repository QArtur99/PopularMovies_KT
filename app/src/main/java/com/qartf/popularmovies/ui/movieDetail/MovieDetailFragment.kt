package com.qartf.popularmovies.ui.movieDetail

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
import com.qartf.popularmovies.utility.Constants.Companion.SNACKBAR_ADD
import com.qartf.popularmovies.utility.Constants.Companion.SNACKBAR_REMOVE
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailFragment : Fragment() {

    private val movieDetailViewModel: MovieDetailViewModel by viewModel()
    private lateinit var binding: FragmentDetailBinding
    private var onFabClicked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.movieDetailViewModel = movieDetailViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        movieDetailViewModel.showReviews.observe(viewLifecycleOwner, Observer {
            it?.let {
                val reviewDialog = ReviewDialog()
                reviewDialog.show(parentFragmentManager, ReviewDialog::class.simpleName)
                movieDetailViewModel.onShowReviews(null)
            }
        })

        movieDetailViewModel.showTrailers.observe(viewLifecycleOwner, Observer {
            it?.let {
                val reviewDialog = VideoDialog()
                reviewDialog.show(parentFragmentManager, VideoDialog::class.simpleName)
                movieDetailViewModel.onShowTrailers(null)
            }
        })

        movieDetailViewModel.favorite.observe(viewLifecycleOwner, Observer {
            if (it && onFabClicked) showSnackBar(SNACKBAR_ADD)
            else if (onFabClicked) showSnackBar(SNACKBAR_REMOVE)
        })

        movieDetailViewModel.listItem.observe(viewLifecycleOwner, Observer {
            it?.let { onFabClicked = false }
        })

        movieDetailViewModel.fabButton.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) onFabClicked = true
                movieDetailViewModel.onFabButtonClick(null)
            }
        })

        return binding.root
    }

    private fun showSnackBar(snackBarId: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            when (snackBarId) {
                SNACKBAR_ADD -> getString(R.string.editor_insert_movie_successful)
                SNACKBAR_REMOVE -> getString(R.string.editor_delete_product_successful)
                else -> getString(R.string.editor_delete_product_successful)
            },
            Snackbar.LENGTH_SHORT
        ).show()
    }
}