package com.artf.popularmovies.gridView

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.artf.popularmovies.MovieDetailActivity
import com.artf.popularmovies.R
import com.artf.popularmovies.database.MovieDatabase
import com.artf.popularmovies.databinding.FragmentGridViewBinding
import com.artf.popularmovies.domain.Movie
import com.artf.popularmovies.movieDetail.MovieDetailViewModel
import com.artf.popularmovies.movieDetail.MovieDetailViewModelFactory
import com.artf.popularmovies.repository.NetworkState
import com.artf.popularmovies.repository.Status
import com.artf.popularmovies.utility.Constants.Companion.INTENT_LIST_ITEM
import com.artf.popularmovies.utility.Constants.Result
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class GridViewFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gridViewViewModel: GridViewViewModel
    private lateinit var movieDetailViewModel: MovieDetailViewModel
    private lateinit var application: Application
    private lateinit var binding: FragmentGridViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_grid_view, container, false
        )

        application = requireNotNull(this.activity).application
        val (columns, sortBy) = setSharedPreferences(application)
        val movieDatabase = MovieDatabase.getInstance(application).movieDatabaseDao
        val viewModelFactory = MovieDetailViewModelFactory(movieDatabase)
        movieDetailViewModel =
            ViewModelProviders.of(activity!!, viewModelFactory).get(MovieDetailViewModel::class.java)

        val viewModelFactory2 = GridViewViewModelFactory(movieDatabase, columns, sortBy, "1")
        gridViewViewModel =
            ViewModelProviders.of(this, viewModelFactory2).get(GridViewViewModel::class.java)

        binding.gridViewViewModel = gridViewViewModel
        binding.lifecycleOwner = this


        gridViewViewModel.columns.observe(viewLifecycleOwner, Observer {
            it?.let { properties ->
                setLayoutManager(properties)
            }
        })

        gridViewViewModel.networkState.observe(viewLifecycleOwner, Observer {
            it?.let { properties ->
                bindNetworkState(properties)
            }
        })

        gridViewViewModel.listItem.observe(viewLifecycleOwner, Observer {
            it?.let { listItem ->
                if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    movieDetailViewModel.setListItem(listItem)
                }else {
                    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                    val jsonAdapter = moshi.adapter<Movie>(Movie::class.java)
                    val listItemString: String = jsonAdapter.toJson(listItem)
                    val intent = Intent(activity, MovieDetailActivity::class.java)
                    intent.putExtra(INTENT_LIST_ITEM, listItemString)
                    application.startActivity(intent)
                }
                //gridViewViewModel.onRecyclerItemClick(null)
            }
        })


        binding.recyclerView.adapter = GridViewPagingAdapter(
            GridViewPagingAdapter.OnClickListener { product -> gridViewViewModel.onRecyclerItemClick(product) },
            GridViewPagingAdapter.OnNetworkStateClickListener { gridViewViewModel.retry() }
        )

        initSwipeToRefresh()
        setLayoutManager(columns)

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


    fun setLayoutManager(columns: Int) {
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
            R.id.action_sortBy -> openBottomDialog()
            R.id.one_column -> sharedPreferences.edit().putInt(
                getString(R.string.pref_number_of_columns_key), 1
            ).apply()
            R.id.two_columns -> sharedPreferences.edit().putInt(
                getString(R.string.pref_number_of_columns_key), 2
            ).apply()
            R.id.three_columns -> sharedPreferences.edit().putInt(
                getString(R.string.pref_number_of_columns_key), 3
            ).apply()
            R.id.four_columns -> sharedPreferences.edit().putInt(
                getString(R.string.pref_number_of_columns_key), 4
            ).apply()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun openBottomDialog() {
        val dialog = SettingsBottomSheetDialog(activity!!)
        dialog.create()
        dialog.show()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            getString(R.string.pref_number_of_columns_key) -> {
                gridViewViewModel.onColumnChanged(
                    sharedPreferences?.getInt(key, resources.getInteger(R.integer.number_of_columns))!!
                )
            }
            getString(R.string.pref_sort_by_key) -> {
                val sortBy = sharedPreferences?.getString(key, getString(R.string.pref_sort_by_most_popular))
                when (sortBy) {
                    getString(R.string.pref_sort_by_favorite) -> gridViewViewModel.onSortByChanged(sortBy)
                    else -> gridViewViewModel.onSortByChanged(sortBy!!)
                }
            }
        }
    }

    fun setSharedPreferences(application: Application): Result {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        val columns = sharedPreferences.getInt(
            getString(R.string.pref_number_of_columns_key), resources.getInteger(R.integer.number_of_columns)
        )
        val sortBy = sharedPreferences.getString(
            getString(R.string.pref_sort_by_key), getString(R.string.pref_sort_by_most_popular)
        )
        return Result(columns, sortBy!!)
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
                if (2 > binding.recyclerView.adapter?.itemCount!!) {
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
                if (1 > binding.recyclerView.adapter?.itemCount!!) {
                    binding.emptyView.visibility = View.VISIBLE
                    binding.emptyTitleText.text = getString(R.string.no_favorite)
                    binding.emptySubtitleText.text = getString(R.string.no_favorite_sub_text)
                }
            }
        }
    }

    private fun checkConnection(): Boolean {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

}