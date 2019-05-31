package com.artf.popularmovies.movieDetail

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.artf.popularmovies.R
import com.artf.popularmovies.databinding.DialogReviewBinding
import com.artf.popularmovies.databinding.DialogVideoBinding

class MovieDetailFragment : Fragment() {

    private lateinit var binding: com.artf.popularmovies.databinding.FragmentDetailBinding
    private lateinit var application: Application

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail, container, false
        )

        application = requireNotNull(this.activity).application
        val viewModelFactory = MovieDetailViewModelFactory()
        val movieDetailViewModel: MovieDetailViewModel =
            ViewModelProviders.of(activity!!, viewModelFactory).get(MovieDetailViewModel::class.java)

        binding.movieDetailViewModel = movieDetailViewModel
        binding.lifecycleOwner = this

        movieDetailViewModel.showReviews.observe(viewLifecycleOwner, Observer {
            it?.let { properties ->
                if(properties){
                    val binding = DialogReviewBinding.inflate(LayoutInflater.from(activity))
                    binding.movieDetailViewModel = movieDetailViewModel
                    binding.recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
                    binding.recyclerView.adapter = ReviewAdapter(ReviewAdapter.OnClickListener { product ->
                        movieDetailViewModel.onReviewListItemClick(product)
                    })
                    setDetailDialog(binding)
                    movieDetailViewModel.onShowReviews(false)
                }
            }
        })

        movieDetailViewModel.showTrailers.observe(viewLifecycleOwner, Observer {
            it?.let { properties ->
                if(properties){
                    val binding = DialogVideoBinding.inflate(LayoutInflater.from(activity))
                    binding.movieDetailViewModel = movieDetailViewModel
                    binding.recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
                    binding.recyclerView.adapter = VideoAdapter(VideoAdapter.OnClickListener { product ->
                        movieDetailViewModel.onVideoListItemClick(product)
                    })
                    setDetailDialog(binding)
                    movieDetailViewModel.onShowTrailers(false)
                }
            }
        })

        movieDetailViewModel.reviewListItem.observe(viewLifecycleOwner, Observer {
            it?.let { properties ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(properties.url))
                if (intent.resolveActivity(activity!!.packageManager) != null) {
                    startActivity(intent)
                }
            }
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


        return binding.root
    }

    private fun setDetailDialog(binding: ViewDataBinding) {
        val dialog = AlertDialog.Builder(activity!!)
            .setView(binding.root)
            .create()
        dialog.show()
        dialog.setCanceledOnTouchOutside(true)
    }

}