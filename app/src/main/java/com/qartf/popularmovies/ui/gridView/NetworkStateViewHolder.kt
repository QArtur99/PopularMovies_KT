package com.qartf.popularmovies.ui.gridView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.qartf.popularmovies.databinding.NetworkStateItemBinding
import com.qartf.popularmovies.domain.state.NetworkState
import com.qartf.popularmovies.domain.state.Status

class NetworkStateViewHolder constructor(val binding: NetworkStateItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        clickListener: GridViewPagingAdapter.OnNetworkStateClickListener,
        networkState: NetworkState?
    ) {
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
