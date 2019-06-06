package com.qartf.popularmovies.movieDetail

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
import com.google.android.material.snackbar.Snackbar
import com.qartf.popularmovies.R
import com.qartf.popularmovies.databinding.DialogReviewBinding
import com.qartf.popularmovies.databinding.DialogVideoBinding
import com.qartf.popularmovies.databinding.FragmentDetailBinding
import com.qartf.popularmovies.utility.ServiceLocator

class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var application: Application
    private var onFabClickCounter: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail, container, false
        )

        application = requireNotNull(this.activity).application
        val repository = ServiceLocator.instance(application).getRepository()
        val viewModelFactory = MovieDetailViewModelFactory(repository)
        val movieDetailViewModel: MovieDetailViewModel =
            ViewModelProviders.of(activity!!, viewModelFactory).get(MovieDetailViewModel::class.java)

        binding.movieDetailViewModel = movieDetailViewModel
        binding.lifecycleOwner = this

        movieDetailViewModel.showReviews.observe(viewLifecycleOwner, Observer {
            it?.let { properties ->
                if (properties) {
                    val binding = DialogReviewBinding.inflate(LayoutInflater.from(activity))
                    binding.movieDetailViewModel = movieDetailViewModel
                    binding.recyclerView.addItemDecoration(
                        DividerItemDecoration(
                            activity,
                            DividerItemDecoration.VERTICAL
                        )
                    )
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
                if (properties) {
                    val binding = DialogVideoBinding.inflate(LayoutInflater.from(activity))
                    binding.movieDetailViewModel = movieDetailViewModel
                    binding.recyclerView.addItemDecoration(
                        DividerItemDecoration(
                            activity,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    binding.recyclerView.adapter = VideoAdapter(VideoAdapter.OnClickListener { product ->
                        movieDetailViewModel.onVideoListItemClick(product)
                    })
                    setDetailDialog(binding)
                    movieDetailViewModel.onShowTrailers(false)
                }
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

    private fun setDetailDialog(binding: ViewDataBinding) {
        val dialog = AlertDialog.Builder(activity!!)
            .setView(binding.root)
            .create()
        dialog.show()
        dialog.setCanceledOnTouchOutside(true)
    }

}