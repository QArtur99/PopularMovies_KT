package com.qartf.popularmovies.gridView

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Parcelable
import android.preference.PreferenceManager
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.qartf.popularmovies.MovieDetailActivity
import com.qartf.popularmovies.R
import com.qartf.popularmovies.databinding.FragmentGridViewBinding
import com.qartf.popularmovies.movieDetail.MovieDetailViewModel
import com.qartf.popularmovies.movieDetail.MovieDetailViewModelFactory
import com.qartf.popularmovies.repository.NetworkState
import com.qartf.popularmovies.repository.Status
import com.qartf.popularmovies.utility.Constants
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
import com.qartf.popularmovies.utility.Result
import com.qartf.popularmovies.utility.ServiceLocator
import com.qartf.popularmovies.utility.convertToString


class GridViewFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gridViewViewModel: GridViewViewModel
    private lateinit var movieDetailViewModel: MovieDetailViewModel
    private lateinit var application: Application
    private lateinit var binding: FragmentGridViewBinding
    private var sortBy: String = ""
    private var adapterItemCount: Int = 0
    private var savedInstanceState: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        this.savedInstanceState = savedInstanceState
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_grid_view, container, false
        )

        application = requireNotNull(this.activity).application
        val repository = ServiceLocator.instance(application).getRepository()
        val prefResult = setSharedPreferences(application)

        val movieDetailViewModelFactory = MovieDetailViewModelFactory(repository)
        movieDetailViewModel =
            ViewModelProviders.of(activity!!, movieDetailViewModelFactory).get(MovieDetailViewModel::class.java)

        val gridViewViewModelFactory = GridViewViewModelFactory(repository, prefResult)
        gridViewViewModel =
            ViewModelProviders.of(this, gridViewViewModelFactory).get(GridViewViewModel::class.java)

        binding.gridViewViewModel = gridViewViewModel
        binding.lifecycleOwner = this


        gridViewViewModel.columns.observe(viewLifecycleOwner, Observer {
            it?.let { properties ->
                setLayoutManager(properties)
            }
        })

        gridViewViewModel.sortBy.observe(viewLifecycleOwner, Observer {
            it?.let { value ->
                this.sortBy = value.sortBy
            }
        })

        gridViewViewModel.networkState.observe(viewLifecycleOwner, Observer {
            it?.let { properties ->
                bindNetworkState(properties)
            }
        })

        gridViewViewModel.listItem.observe(viewLifecycleOwner, Observer {
            it?.let { listItem ->
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    movieDetailViewModel.setListItem(listItem)
                } else {
                    val intent = Intent(activity, MovieDetailActivity::class.java)
                    intent.putExtra(INTENT_LIST_ITEM_ID, convertToString(listItem))
                    application.startActivity(intent)
                }
                //gridViewViewModel.onRecyclerItemClick(null)
            }
        })

        gridViewViewModel.posts.observe(viewLifecycleOwner, Observer {
            it?.let { listItem ->
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (listItem.isNotEmpty()) {
                        movieDetailViewModel.setFirstListItem(listItem.first())
                    }
                }
                adapterItemCount = listItem.size
                if (this.sortBy == Constants.SORT_BY_FAVORITE && adapterItemCount > 0) {
                    gridViewViewModel.refresh()
                }
            }
        })


        binding.recyclerView.adapter = GridViewPagingAdapter(
            GridViewPagingAdapter.OnClickListener { product -> gridViewViewModel.onRecyclerItemClick(product) },
            GridViewPagingAdapter.OnSizeListener { adapterItemCount > 0 },
            GridViewPagingAdapter.OnNetworkStateClickListener { gridViewViewModel.retry() }
        )

        initSwipeToRefresh()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun initSwipeToRefresh() {
        gridViewViewModel.refreshState.observe(viewLifecycleOwner, Observer {
            it?.let { properties ->
                binding.swipeRefresh.isRefreshing = properties == NetworkState.LOADING
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            gridViewViewModel.refresh()
        }
    }


    private fun setLayoutManager(columns: Int) {
        val manager = GridLayoutManager(application, columns)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = 1
        }
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu, menu)
        menu.findItem(R.id.action_favorite).isVisible =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> movieDetailViewModel.onFabButtonClick(true)
            R.id.action_refresh -> gridViewViewModel.refresh()
            //R.id.action_sortBy -> openBottomDialog()
            R.id.favorite -> sharedPreferences.edit().putString(SORT_BY_KEY, SORT_BY_FAVORITE).apply()
            R.id.popularity -> sharedPreferences.edit().putString(SORT_BY_KEY, SORT_BY_POPULARITY).apply()
            R.id.releaseDate -> sharedPreferences.edit().putString(SORT_BY_KEY, SORT_BY_RELEASE_DATE).apply()
            R.id.revenue -> sharedPreferences.edit().putString(SORT_BY_KEY, SORT_BY_REVENUE).apply()
            R.id.voteAverage -> sharedPreferences.edit().putString(SORT_BY_KEY, SORT_BY_VOTE_AVERAGE).apply()
            R.id.voteCount -> sharedPreferences.edit().putString(SORT_BY_KEY, SORT_BY_VOTE_COUNT).apply()
            R.id.one_column -> sharedPreferences.edit().putInt(NUMBER_OF_COLUMNS_KEY, 1).apply()
            R.id.two_columns -> sharedPreferences.edit().putInt(NUMBER_OF_COLUMNS_KEY, 2).apply()
            R.id.three_columns -> sharedPreferences.edit().putInt(NUMBER_OF_COLUMNS_KEY, 3).apply()
            R.id.four_columns -> sharedPreferences.edit().putInt(NUMBER_OF_COLUMNS_KEY, 4).apply()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun openBottomDialog() {
        val dialog = SettingsBottomSheetDialog(activity!!)
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
                val sortBy = sharedPreferences.getString(key, SORT_BY_POPULARITY)
                when (sortBy) {
                    SORT_BY_FAVORITE -> gridViewViewModel.onSortByChanged(sortBy)
                    else -> gridViewViewModel.onSortByChanged(sortBy)
                }
            }
            SORT_BY_GENRE_KEY -> {
                val sortByGenre = sharedPreferences.getString(key, SORT_BY_GENRE_DEFAULT)
                gridViewViewModel.onSortByGenreChanged(sortByGenre)
            }
        }
    }

    fun setSharedPreferences(application: Application): Result {
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
            Status.RUNNING -> {
                binding.emptyView.visibility = View.GONE
            }
            Status.FAILED -> {
                if (1 > adapterItemCount) {
                    binding.emptyView.visibility = View.VISIBLE
                    if (checkConnection()) {
                        binding.emptyTitleText.text = getString(R.string.server_problem)
                        binding.emptySubtitleText.text = getString(R.string.server_problem_sub_text)
                    } else {
                        binding.emptyTitleText.text = getString(R.string.no_connection)
                        binding.emptySubtitleText.text = getString(R.string.no_connection_sub_text)
                    }
                }
            }
            Status.SUCCESS -> {
                binding.emptyView.visibility = View.GONE
            }
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

    private fun checkConnection(): Boolean {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }


    override fun onResume() {
        super.onResume()
        if (savedInstanceState != null) {
            val listState: Parcelable? = savedInstanceState!!.getParcelable(RECYCLER_VIEW_STATE_ID)
            binding.recyclerView.layoutManager?.onRestoreInstanceState(listState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val listState = binding.recyclerView.layoutManager?.onSaveInstanceState()
        outState.putParcelable(RECYCLER_VIEW_STATE_ID, listState)
        super.onSaveInstanceState(outState)
    }
}