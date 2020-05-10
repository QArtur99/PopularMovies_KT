package com.qartf.popularmovies.ui.movieDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.qartf.popularmovies.databinding.DialogReviewBinding
import com.qartf.popularmovies.utility.Utility
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewDialog : DialogFragment() {

    private val movieDetailViewModel: MovieDetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogReviewBinding.inflate(LayoutInflater.from(requireActivity()))
        binding.movieDetailViewModel = movieDetailViewModel
        binding.recyclerView.addItemDecoration(getDivider())
        binding.recyclerView.adapter = ReviewAdapter(ReviewAdapter.OnClickListener { product ->
            movieDetailViewModel.onReviewListItemClick(product)
        })

        movieDetailViewModel.reviewListItem.observe(viewLifecycleOwner, Observer {
            it?.let { properties ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(properties.url))
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                }
            }
        })

        Utility.onCreateDialog(requireActivity(), dialog!!, binding.root, 400, 400)
        return binding.root
    }

    private fun getDivider(): DividerItemDecoration {
        return DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
    }
}