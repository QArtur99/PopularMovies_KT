package com.qartf.popularmovies.utility

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.qartf.popularmovies.R
import com.qartf.popularmovies.domain.Movie
import com.qartf.popularmovies.domain.ReviewContainer
import com.qartf.popularmovies.domain.VideoContainer
import com.qartf.popularmovies.gridView.GridViewPagingAdapter
import com.qartf.popularmovies.movieDetail.MovieDetailViewModel
import com.qartf.popularmovies.movieDetail.ReviewAdapter
import com.qartf.popularmovies.movieDetail.VideoAdapter
import com.qartf.popularmovies.repository.NetworkState


@BindingAdapter("listData")
fun bindMoviesRecyclerView(recyclerView: RecyclerView, data: Any?) {
    when (data) {
        is PagedList<*> -> {
            val adapter = recyclerView.adapter as GridViewPagingAdapter
            val pagedList: PagedList<Movie> = data as PagedList<Movie>
            adapter.submitList(pagedList)
            adapter.notifyDataSetChanged()
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

    if (imgUrl.isNullOrEmpty()) {
        val drawable = ContextCompat.getDrawable(imgView.context, R.drawable.ic_broken_image)!!
        imgView.setImageDrawable(drawable)
        return
    }

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

@BindingAdapter("imageUrlDetail", "liveData")
fun bindImageDetail(imgView: ImageView, movieDetailViewModel: MovieDetailViewModel?, liveData: Movie?) {
    val imgUrl: String? = movieDetailViewModel?.listItem?.value?.poster_path
    if (imgUrl.isNullOrEmpty()) {
        val drawable = ContextCompat.getDrawable(imgView.context, R.drawable.ic_broken_image)!!
        movieDetailViewModel?.setPoster(drawable)
        imgView.setImageDrawable(drawable)
        return
    }

    val posterURL = "http://image.tmdb.org/t/p/w342/$imgUrl"
    Glide.with(imgView.context)
        .load(posterURL)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                p0: GlideException?,
                p1: Any?,
                p2: Target<Drawable>?,
                p3: Boolean
            ): Boolean {
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

@BindingAdapter("loadBackground")
fun loadBackground(linearLayout: LinearLayout, drawable: Drawable?) {
    if (drawable != null) {
        val layers = arrayOfNulls<Drawable>(2)
        layers[0] = drawable
        layers[1] =
            ContextCompat.getDrawable(linearLayout.context, R.drawable.background_transparent)
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
        floatingActionButton.setImageResource(com.qartf.popularmovies.R.drawable.ic_favorite)
    } else {
        floatingActionButton.setImageResource(com.qartf.popularmovies.R.drawable.ic_favorite_border)
    }
}

@BindingAdapter("enableReview")
fun enableReview(button: Button, reviewContainer: ReviewContainer?) {
    button.isEnabled = reviewContainer?.reviews?.isNotEmpty() ?: false
}

@BindingAdapter("enableVideo")
fun enableVideo(button: Button, reviewContainer: VideoContainer?) {
    button.isEnabled = reviewContainer?.videos?.isNotEmpty() ?: false
}

@BindingAdapter("showOverview")
fun showOverview(textView: TextView, showOverview: Boolean?) {
    if(showOverview == true) textView.maxLines = Int.MAX_VALUE else textView.maxLines = 4
}


