package com.artf.popularmovies.gridView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artf.popularmovies.databinding.RowMovieItemBinding
import com.artf.popularmovies.domain.Movie

class GridViewAdapter(private val clickListener: OnClickListener) : ListAdapter<Movie,
        GridViewAdapter.ViewHolder>(GridViewDiffCallback) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(clickListener, product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowMovieItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    class ViewHolder constructor(val binding: RowMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OnClickListener, item: Movie) {
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
}