package com.qartf.popularmovies.gridView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.qartf.popularmovies.R
import com.qartf.popularmovies.databinding.NetworkStateItemBinding
import com.qartf.popularmovies.databinding.RowMovieItemBinding
import com.qartf.popularmovies.domain.Movie
import com.qartf.popularmovies.repository.NetworkState

class GridViewPagingAdapter(
    private val clickListener: OnClickListener,
    private val sizeListener: OnSizeListener,
    private val networkStateClickListener: OnNetworkStateClickListener
) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(GridViewDiffCallback) {

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.row_movie_item ->
                (holder as MovieViewHolder).bind(clickListener, getItem(position))
            R.layout.network_state_item ->
                (holder as NetworkStateViewHolder).bind(networkStateClickListener, networkState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.row_movie_item ->
                MovieViewHolder(RowMovieItemBinding.inflate(LayoutInflater.from(parent.context)))
            R.layout.network_state_item ->
                NetworkStateViewHolder(NetworkStateItemBinding.inflate(LayoutInflater.from(parent.context)))
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    private fun hasExtraRow() =
        networkState != null && networkState != NetworkState.LOADED && sizeListener.onClick()

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) R.layout.network_state_item else R.layout.row_movie_item
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    class MovieViewHolder constructor(val binding: RowMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OnClickListener, item: Movie?) {
            binding.movie = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    companion object GridViewDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    class OnClickListener(val clickListener: (productId: Movie) -> Unit) {
        fun onClick(product: Movie) = clickListener(product)
    }

    class OnSizeListener(val clickListener: () -> Boolean) {
        fun onClick() : Boolean = clickListener()
    }

    class OnNetworkStateClickListener(val clickListener: () -> Unit) {
        fun onClick() = clickListener()
    }
}