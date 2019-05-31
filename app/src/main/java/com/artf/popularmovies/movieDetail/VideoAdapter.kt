package com.artf.popularmovies.movieDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artf.popularmovies.databinding.RowDetailTrailerBinding
import com.artf.popularmovies.domain.Video

class VideoAdapter(private val clickListener: OnClickListener) : ListAdapter<Video,
        VideoAdapter.ViewHolder>(GridViewDiffCallback) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(clickListener, product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowDetailTrailerBinding.inflate(LayoutInflater.from(parent.context)))
    }

    class ViewHolder constructor(val binding: RowDetailTrailerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OnClickListener, item: Video) {
            binding.video = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    companion object GridViewDiffCallback : DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem == newItem
        }
    }

    class OnClickListener(val clickListener: (productId: Video) -> Unit) {
        fun onClick(product: Video) = clickListener(product)
    }
}