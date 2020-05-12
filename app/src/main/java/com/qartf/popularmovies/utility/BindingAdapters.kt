package com.qartf.popularmovies.utility

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
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
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.qartf.popularmovies.R
import com.qartf.popularmovies.data.model.Movie
import com.qartf.popularmovies.data.model.ReviewContainer
import com.qartf.popularmovies.data.model.VideoContainer
import com.qartf.popularmovies.ui.gridView.GridViewPagingAdapter
import com.qartf.popularmovies.ui.movieDetail.MovieDetailViewModel
import com.qartf.popularmovies.ui.movieDetail.ReviewAdapter
import com.qartf.popularmovies.ui.movieDetail.VideoAdapter
import com.qartf.popularmovies.domain.state.NetworkState
import com.qartf.popularmovies.utility.Constants.FabStatus

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
                p0: Drawable?,
                p1: Any?,
                p2: Target<Drawable>?,
                p3: DataSource?,
                p4: Boolean
            ): Boolean {
                movieDetailViewModel.setPoster(p0!!)
                return false
            }
        })
        .apply(
            RequestOptions()
                // .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
        )
        .into(imgView)
}

@BindingAdapter("loadBackground")
fun loadBackground(view: View, drawable: Drawable?) {
    if (drawable != null) {
        val layers = arrayOfNulls<Drawable>(2)
        layers[0] = Utility.resize(view.width, view.height, drawable, view.context.resources)
        layers[1] = ContextCompat.getDrawable(view.context, R.drawable.background_transparent)
        val layerDrawable = LayerDrawable(layers)
        view.background = layerDrawable
    }
}

@BindingAdapter("visibleIf")
fun changeVisibility(view: View, visible: FabStatus?) {
    when (visible) {
        FabStatus.TOP -> view.visibility = if (view.id == R.id.fabTop) View.VISIBLE else View.GONE
        FabStatus.BOTTOM -> view.visibility = if (view.id == R.id.fabBottom) View.VISIBLE else View.GONE
        else -> view.visibility = View.GONE
    }
}

@BindingAdapter("appBarLayoutOpen", "item")
fun onAppBarLayoutOpen(collapsingToolbarLayout: CollapsingToolbarLayout, appBarLayoutOpen: FabStatus?, item: Movie?) {
    when (appBarLayoutOpen) {
        FabStatus.TOP -> collapsingToolbarLayout.title = ""
        FabStatus.BOTTOM -> collapsingToolbarLayout.title = item?.original_title
        else -> collapsingToolbarLayout.title = ""
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
    if (showOverview == true) textView.maxLines = Int.MAX_VALUE else textView.maxLines = 4
}

@BindingAdapter("showGenres")
fun setGenreChips(chipGroup: ChipGroup, movie: Movie?) {
    movie?.let {
        val inflater = LayoutInflater.from(chipGroup.context)
        val children = mutableListOf<Chip>()
        it.genre_ids.map { genreId ->
            val genre = Constants.GENRE_MAP[genreId]
            if (genre.isNullOrBlank().not()) {
                val chip = inflater.inflate(R.layout.genre, chipGroup, false) as Chip
                chip.text = genre
                chip.tag = genreId
                children.add(chip)
            }
        }

        chipGroup.removeAllViews()
        for (chip in children) {
            chipGroup.addView(chip)
        }
    }
}
