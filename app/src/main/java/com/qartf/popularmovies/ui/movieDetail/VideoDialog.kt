package com.qartf.popularmovies.ui.movieDetail

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
import com.qartf.popularmovies.databinding.DialogVideoBinding
import com.qartf.popularmovies.utility.Utility
import com.qartf.popularmovies.utility.extension.getVmFactory

class VideoDialog : DialogFragment() {

    private val movieDetailViewModel by activityViewModels<MovieDetailViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogVideoBinding.inflate(LayoutInflater.from(activity))
        binding.movieDetailViewModel = movieDetailViewModel
        binding.recyclerView.addItemDecoration(getDivider())
        binding.recyclerView.adapter = VideoAdapter(VideoAdapter.OnClickListener { product ->
            movieDetailViewModel.onVideoListItemClick(product)
        })

        movieDetailViewModel.videoListItem.observe(viewLifecycleOwner, Observer {
            it?.let { properties ->
                val youTubeBase = "https://www.youtube.com/watch?v="
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youTubeBase + properties.key))
                if (intent.resolveActivity(activity!!.packageManager) != null) {
                    startActivity(intent)
                }
            }
        })

        Utility.onCreateDialog(activity!!, dialog!!, binding.root, 400, 400)
        return binding.root
    }

    private fun getDivider(): DividerItemDecoration {
        return DividerItemDecoration(
            activity,
            DividerItemDecoration.VERTICAL
        )
    }
}