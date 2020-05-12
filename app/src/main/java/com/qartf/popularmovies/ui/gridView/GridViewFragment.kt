package com.qartf.popularmovies.ui.gridView

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.qartf.popularmovies.R
import com.qartf.popularmovies.data.model.Movie
import com.qartf.popularmovies.data.model.Result
import com.qartf.popularmovies.databinding.FragmentGridViewBinding
import com.qartf.popularmovies.domain.state.NetworkState
import com.qartf.popularmovies.domain.state.Status
import com.qartf.popularmovies.domain.mapper.convertToString
import com.qartf.popularmovies.ui.MovieDetailActivity
import com.qartf.popularmovies.ui.movieDetail.MovieDetailViewModel
import com.qartf.popularmovies.utility.ConnectionUtils.isConnectedToInternet
import com.qartf.popularmovies.utility.Constants.Companion.INTENT_LIST_ITEM_ID
import com.qartf.popularmovies.utility.Constants.Companion.NUMBER_OF_COLUMNS_DEFAULT
import com.qartf.popularmovies.utility.Constants.Companion.NUMBER_OF_COLUMNS_KEY
import com.qartf.popularmovies.utility.Constants.Companion.RECYCLER_VIEW_STATE_ID
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_FAVORITE
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_GENRE_DEFAULT
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_GENRE_KEY
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_KEY
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_POPULARITY
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_RELEASE_DATE
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_REVENUE
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_VOTE_AVERAGE
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_VOTE_COUNT
import com.qartf.popularmovies.utility.Constants.Companion.TOOLBAR_IMAGE
import org.koin.androidx.viewmodel.ext.android.viewModel

class GridViewFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentGridViewBinding
    private var sortBy: String = ""
    private var adapterItemCount: Int = 0
    private var savedInstanceState: Bundle? = null
    private var activityWithOptions = false

    private val gridViewViewModel: GridViewViewModel by viewModel()
    private val movieDetailViewModel: MovieDetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setSharedPreferences(requireActivity().application)
        this.savedInstanceState = savedInstanceState
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_grid_view, container, false)
        binding.gridViewViewModel = gridViewViewModel
        binding.lifecycleOwner = this

        gridViewViewModel.columns.observe(viewLifecycleOwner, Observer {
            it?.let { setLayoutManager(it) }
        })

        gridViewViewModel.discoverMovie.observe(viewLifecycleOwner, Observer {
            it?.let { this.sortBy = it.sortBy }
        })

        gridViewViewModel.networkState.observe(viewLifecycleOwner, Observer {
            it?.let { bindNetworkState(it) }
        })

        gridViewViewModel.listItem.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            val (v, listItem) = it
            if (isLandscape()) {
                movieDetailViewModel.setListItem(listItem)
                return@Observer
            }
            if (activityWithOptions) {
                requireActivity().startActivity(getDetailIntent(listItem), getOptions(v))
            } else {
                requireActivity().startActivity(getDetailIntent(listItem))
            }
        })

        gridViewViewModel.posts.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            if (isLandscape() && it.isNotEmpty()) {
                movieDetailViewModel.setFirstListItem(it.first())
            }
            adapterItemCount = it.size
            if (this.sortBy == SORT_BY_FAVORITE && adapterItemCount > 0) {
                gridViewViewModel.refresh()
            }
        })

        binding.recyclerView.adapter = GridViewPagingAdapter(
            GridViewPagingAdapter.OnClickListener { gridViewViewModel.onRecyclerItemClick(it) },
            GridViewPagingAdapter.OnSizeListener { adapterItemCount > 0 },
            GridViewPagingAdapter.OnNetworkStateClickListener { gridViewViewModel.retry() }
        )

        initSwipeToRefresh()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun getDetailIntent(listItem: Movie): Intent {
        val intent = Intent(activity, MovieDetailActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(INTENT_LIST_ITEM_ID, convertToString(listItem))
        return intent
    }

    private fun getOptions(v: View): Bundle? {
        return ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            Pair(v.findViewById(R.id.itemImage), TOOLBAR_IMAGE)
        ).toBundle()
    }

    private fun initSwipeToRefresh() {
        gridViewViewModel.refreshState.observe(viewLifecycleOwner, Observer {
            it?.let { binding.swipeRefresh.isRefreshing = it == NetworkState.LOADING }
        })
        binding.swipeRefresh.setOnRefreshListener { gridViewViewModel.refresh() }
    }

    private fun setLayoutManager(columns: Int) {
        val manager = GridLayoutManager(activity, columns)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = 1
        }
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu, menu)
        menu.findItem(R.id.action_favorite).isVisible = isLandscape()
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun isLandscape(): Boolean {
        return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> movieDetailViewModel.onFabButtonClick(true)
            R.id.action_refresh -> gridViewViewModel.refresh()
            // R.id.action_sortBy -> openBottomDialog()
            R.id.favorite -> spPutString(SORT_BY_KEY, SORT_BY_FAVORITE)
            R.id.popularity -> spPutString(SORT_BY_KEY, SORT_BY_POPULARITY)
            R.id.releaseDate -> spPutString(SORT_BY_KEY, SORT_BY_RELEASE_DATE)
            R.id.revenue -> spPutString(SORT_BY_KEY, SORT_BY_REVENUE)
            R.id.voteAverage -> spPutString(SORT_BY_KEY, SORT_BY_VOTE_AVERAGE)
            R.id.voteCount -> spPutString(SORT_BY_KEY, SORT_BY_VOTE_COUNT)
            R.id.one_column -> spPutInt(NUMBER_OF_COLUMNS_KEY, 1)
            R.id.two_columns -> spPutInt(NUMBER_OF_COLUMNS_KEY, 2)
            R.id.three_columns -> spPutInt(NUMBER_OF_COLUMNS_KEY, 3)
            R.id.four_columns -> spPutInt(NUMBER_OF_COLUMNS_KEY, 4)
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun spPutString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun spPutInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    private fun openBottomDialog() {
        val dialog = SettingsBottomSheetDialog(requireActivity())
        dialog.create()
        dialog.show()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        when (key) {
            NUMBER_OF_COLUMNS_KEY -> {
                gridViewViewModel.onColumnChanged(
                    sharedPreferences.getInt(key, NUMBER_OF_COLUMNS_DEFAULT)
                )
            }
            SORT_BY_KEY -> {
                when (val sortBy = sharedPreferences.getString(key, SORT_BY_POPULARITY)!!) {
                    SORT_BY_FAVORITE -> gridViewViewModel.onSortByChanged(sortBy)
                    else -> gridViewViewModel.onSortByChanged(sortBy)
                }
            }
            SORT_BY_GENRE_KEY -> {
                val sortByGenre = sharedPreferences.getString(key, SORT_BY_GENRE_DEFAULT)!!
                gridViewViewModel.onSortByGenreChanged(sortByGenre)
            }
        }
    }

    private fun setSharedPreferences(application: Application): Result {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        val columns = sharedPreferences.getInt(NUMBER_OF_COLUMNS_KEY, NUMBER_OF_COLUMNS_DEFAULT)
        val sortBy = sharedPreferences.getString(SORT_BY_KEY, SORT_BY_POPULARITY)!!
        val genre = sharedPreferences.getString(SORT_BY_GENRE_KEY, SORT_BY_GENRE_DEFAULT)!!
        return Result(columns, sortBy, genre)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun bindNetworkState(networkState: NetworkState) {
        when (networkState.status) {
            Status.RUNNING -> binding.emptyView.visibility = View.GONE
            Status.FAILED -> {
                if (1 > adapterItemCount) {
                    binding.emptyView.visibility = View.VISIBLE
                    if (isConnectedToInternet(requireActivity())) {
                        binding.emptyTitleText.text = getString(R.string.server_problem)
                        binding.emptySubtitleText.text = getString(R.string.server_problem_sub_text)
                    } else {
                        binding.emptyTitleText.text = getString(R.string.no_connection)
                        binding.emptySubtitleText.text = getString(R.string.no_connection_sub_text)
                    }
                }
            }
            Status.SUCCESS -> binding.emptyView.visibility = View.GONE
            Status.DB_EMPTY -> {
                if (1 > adapterItemCount) {
                    binding.emptyView.visibility = View.VISIBLE
                    binding.emptyTitleText.text = getString(R.string.no_favorite)
                    binding.emptySubtitleText.text = getString(R.string.no_favorite_sub_text)
                } else {
                    binding.emptyView.visibility = View.GONE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (savedInstanceState != null) {
            val listState: Parcelable? = savedInstanceState?.getParcelable(RECYCLER_VIEW_STATE_ID)
            binding.recyclerView.layoutManager?.onRestoreInstanceState(listState)
        }
        activityWithOptions = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val listState = binding.recyclerView.layoutManager?.onSaveInstanceState()
        outState.putParcelable(RECYCLER_VIEW_STATE_ID, listState)
        super.onSaveInstanceState(outState)
    }
}