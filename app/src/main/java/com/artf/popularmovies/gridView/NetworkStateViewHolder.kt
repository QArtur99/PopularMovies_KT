package com.artf.popularmovies.gridView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.artf.popularmovies.databinding.NetworkStateItemBinding
import com.artf.popularmovies.repository.NetworkState
import com.artf.popularmovies.repository.Status

class NetworkStateViewHolder constructor(val binding: NetworkStateItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: GridViewPagingAdapter.OnNetworkStateClickListener, networkState: NetworkState?) {
        binding.networkState = networkState
        binding.clickListener = clickListener

        binding.progressBar.visibility = toVisibility(networkState?.status == Status.RUNNING)
        binding.retryButton.visibility = toVisibility(networkState?.status == Status.FAILED)
        binding.errorMsg.visibility = toVisibility(networkState?.msg != null)
        binding.errorMsg.text = networkState?.msg

        binding.executePendingBindings()
    }

    companion object {
        fun toVisibility(constraint: Boolean): Int {
            return if (constraint) View.VISIBLE else View.GONE
        }
    }
}
