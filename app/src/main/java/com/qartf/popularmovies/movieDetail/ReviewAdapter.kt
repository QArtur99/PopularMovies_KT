package com.qartf.popularmovies.movieDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.qartf.popularmovies.databinding.RowDetailReviewBinding
import com.qartf.popularmovies.domain.Review

class ReviewAdapter(private val clickListener: OnClickListener) : ListAdapter<Review,
        ReviewAdapter.ViewHolder>(GridViewDiffCallback) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(clickListener, product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowDetailReviewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    class ViewHolder constructor(val binding: RowDetailReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OnClickListener, item: Review) {
            binding.review = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    companion object GridViewDiffCallback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }

    class OnClickListener(val clickListener: (productId: Review) -> Unit) {
        fun onClick(product: Review) = clickListener(product)
    }
}