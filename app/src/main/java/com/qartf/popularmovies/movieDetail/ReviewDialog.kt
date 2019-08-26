package com.qartf.popularmovies.movieDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.qartf.popularmovies.databinding.DialogReviewBinding
import com.qartf.popularmovies.utility.Utility
import com.qartf.popularmovies.utility.extension.getVmFactory

class ReviewDialog : DialogFragment() {

    private val movieDetailViewModel by activityViewModels<MovieDetailViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DialogReviewBinding.inflate(LayoutInflater.from(activity))
        binding.movieDetailViewModel = movieDetailViewModel
        binding.recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        binding.recyclerView.adapter = ReviewAdapter(ReviewAdapter.OnClickListener { product ->
            movieDetailViewModel.onReviewListItemClick(product)
        })

        movieDetailViewModel.reviewListItem.observe(viewLifecycleOwner, Observer {
            it?.let { properties ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(properties.url))
                if (intent.resolveActivity(activity!!.packageManager) != null) {
                    startActivity(intent)
                }
            }
        })

        Utility.onCreateDialog(activity!!, dialog!!, binding.root, 400, 400)
        return binding.root
    }
}