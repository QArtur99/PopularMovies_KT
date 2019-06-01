package com.artf.popularmovies.utility

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.artf.popularmovies.R
import com.artf.popularmovies.domain.Movie
import com.artf.popularmovies.domain.ReviewContainer
import com.artf.popularmovies.domain.VideoContainer
import com.artf.popularmovies.gridView.GridViewPagingAdapter
import com.artf.popularmovies.movieDetail.MovieDetailViewModel
import com.artf.popularmovies.movieDetail.ReviewAdapter
import com.artf.popularmovies.movieDetail.VideoAdapter
import com.artf.popularmovies.repository.NetworkState
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton


@BindingAdapter("listData")
fun bindMoviesRecyclerView(recyclerView: RecyclerView, data: Any?) {
    when (data) {
        is PagedList<*> -> {
            val adapter = recyclerView.adapter as GridViewPagingAdapter
            if(data.getOrNull(0) is Movie) {
                val pagedList: PagedList<Movie> = data as PagedList<Movie>
                adapter.submitList(pagedList)
                adapter.notifyDataSetChanged()
            }
        }
        is ReviewContainer -> {
            val adapter = recyclerView.adapter as ReviewAdapter
            adapter.submitList(data.reviews)
            adapter.notifyDataSetChanged()
        }
        is VideoContainer -> {
            val adapter = recyclerView.adapter as VideoAdapter
            adapter.submitList(data.videos)
            adapter.notifyDataSetChanged()
        }
    }
}

@BindingAdapter("networkState")
fun setNetworkState(recyclerView: RecyclerView, data: NetworkState?) {
    val adapter = recyclerView.adapter as GridViewPagingAdapter
    adapter.setNetworkState(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val posterURL = "http://image.tmdb.org/t/p/w185/$imgUrl"
        Glide.with(imgView.context)
            .load(posterURL)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}

@BindingAdapter("imageUrlDetail")
fun bindImageDetail(imgView: ImageView, movieDetailViewModel: MovieDetailViewModel?) {
    val imgUrl: String? = movieDetailViewModel?.listItem?.value?.poster_path
    imgUrl?.let {
        val posterURL = "http://image.tmdb.org/t/p/w342/$imgUrl"
        Glide.with(imgView.context)
            .load(posterURL)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Drawable>?, p3: Boolean): Boolean {
                    return false
                }

                override fun onResourceReady(
                    p0: Drawable?, p1: Any?, p2: Target<Drawable>?, p3: DataSource?, p4: Boolean
                ): Boolean {
                    movieDetailViewModel.setPoster(p0!!)
                    return false
                }
            })
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}

@BindingAdapter("loadBackground")
fun loadBackground(linearLayout: LinearLayout, drawable: Drawable?) {
    if (drawable != null) {
        val layers = arrayOfNulls<Drawable>(2)
        layers[0] = drawable
        layers[1] = ContextCompat.getDrawable(linearLayout.context, R.drawable.background_transparent)
        val layerDrawable = LayerDrawable(layers)
        linearLayout.background = layerDrawable
    }
}

@BindingAdapter("visibleIf")
fun changeVisibility(view: View?, visible: Boolean) {
    view?.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("appBarLayoutOpen", "item")
fun onAppBarLayoutOpen(collapsingToolbarLayout: CollapsingToolbarLayout?, appBarLayoutOpen: Boolean, item: Movie?) {
    if (appBarLayoutOpen) {
        collapsingToolbarLayout?.title = ""
    } else {
        collapsingToolbarLayout?.title = item?.original_title
    }
}

@BindingAdapter("fabIcon")
fun setFabIcon(floatingActionButton: FloatingActionButton, isFavorite: Boolean) {
    if (isFavorite) {
        floatingActionButton.setImageResource(R.drawable.ic_favorite)
    } else {
        floatingActionButton.setImageResource(R.drawable.ic_favorite_border)
    }
}