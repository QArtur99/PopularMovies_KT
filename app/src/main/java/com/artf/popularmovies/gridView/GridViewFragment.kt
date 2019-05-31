package com.artf.popularmovies.gridView

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.artf.popularmovies.MovieDetailActivity
import com.artf.popularmovies.databinding.FragmentGridViewBinding
import com.artf.popularmovies.domain.Movie
import com.artf.popularmovies.utility.Constants.ApiStatus
import com.artf.popularmovies.utility.Constants.Companion.INTENT_LIST_ITEM
import com.artf.popularmovies.utility.Constants.Result
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class GridViewFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gridViewViewModel: GridViewViewModel
    private lateinit var application: Application
    private lateinit var binding: FragmentGridViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, com.artf.popularmovies.R.layout.fragment_grid_view, container, false
        )

        application = requireNotNull(this.activity).application
        val (columns, sortBy) = setSharedPreferences(application)

        val viewModelFactory = GridViewViewModelFactory(columns, sortBy, "1")
        gridViewViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(GridViewViewModel::class.java)

        binding.gridViewViewModel = gridViewViewModel
        binding.lifecycleOwner = this


        gridViewViewModel.columns.observe(viewLifecycleOwner, Observer {
            it?.let { properties ->
                setLayoutManager(properties)
            }
        })

        gridViewViewModel.status.observe(viewLifecycleOwner, Observer {
            it?.let { properties ->
                bindStatus(properties)
            }
        })

        gridViewViewModel.listItem.observe(viewLifecycleOwner, Observer {
            it?.let { listItem ->
                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val jsonAdapter = moshi.adapter<Movie>(Movie::class.java)
                val listItemString: String = jsonAdapter.toJson(listItem)
                val intent = Intent(activity, MovieDetailActivity::class.java)
                intent.putExtra(INTENT_LIST_ITEM, listItemString)
                application.startActivity(intent)
            }
        })

        binding.recyclerView.adapter = GridViewAdapter(GridViewAdapter.OnClickListener { product ->
            gridViewViewModel.onRecyclerItemClick(product)
        })

//        binding.swipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
//            fun onRefresh(){
//                var ddd:Int = 0
//            }
//        })

        setLayoutManager(columns)

        setHasOptionsMenu(true)
        return binding.root
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
        inflater.inflate(com.artf.popularmovies.R.menu.menu, menu)
        menu.findItem(com.artf.popularmovies.R.id.action_favorite).isVisible =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.action_favorite -> (activity as MainActivity).addFavorite(null)
//            R.id.action_refresh -> restartLoader(loaderId)
//            R.id.action_sortBy -> openBottomDialog()
            com.artf.popularmovies.R.id.one_column -> sharedPreferences.edit().putInt(
                getString(com.artf.popularmovies.R.string.pref_number_of_columns_key), 1
            ).apply()
            com.artf.popularmovies.R.id.two_columns -> sharedPreferences.edit().putInt(
                getString(com.artf.popularmovies.R.string.pref_number_of_columns_key), 2
            ).apply()
            com.artf.popularmovies.R.id.three_columns -> sharedPreferences.edit().putInt(
                getString(com.artf.popularmovies.R.string.pref_number_of_columns_key), 3
            ).apply()
            com.artf.popularmovies.R.id.four_columns -> sharedPreferences.edit().putInt(
                getString(com.artf.popularmovies.R.string.pref_number_of_columns_key), 4
            ).apply()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            getString(com.artf.popularmovies.R.string.pref_number_of_columns_key) -> {
                gridViewViewModel.onColumnChanged(
                    sharedPreferences?.getInt(
                        key,
                        resources.getInteger(com.artf.popularmovies.R.integer.number_of_columns)
                    )!!
                )
                //setAdapter(columns, moviesAdapter.getData())
            }
//            getString(R.string.pref_sort_by_key) -> {
//                pageNoInteger = 1
//                sortBy = sharedPreferences?.getString(key,getString(R.string.pref_sort_by_most_popular_default)
//                )
//                if (sortBy == getString(R.string.pref_sort_by_favorite)) {
//                    swipyRefreshLayout.setOnRefreshListener(null)
//                    restartLoader(0)
//                } else {
//                    swipyRefreshLayout.setOnRefreshListener(this)
//                    restartLoader(1)
//                }
//            }

        }
    }

    fun setSharedPreferences(application: Application): Result {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        val columns = sharedPreferences.getInt(
            getString(com.artf.popularmovies.R.string.pref_number_of_columns_key),
            resources.getInteger(com.artf.popularmovies.R.integer.number_of_columns)
        )
        val sortBy = sharedPreferences.getString(
            getString(com.artf.popularmovies.R.string.pref_sort_by_key),
            getString(com.artf.popularmovies.R.string.pref_sort_by_most_popular_default)
        )
        return Result(columns, sortBy!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }


    private fun bindStatus(status: ApiStatus) {
        when (status) {
            ApiStatus.LOADING -> {
                binding.emptyView.visibility = View.GONE
                binding.loadingIndicator.visibility = View.VISIBLE
            }
            ApiStatus.ERROR -> {
                binding.emptyView.visibility = View.VISIBLE
                binding.loadingIndicator.visibility = View.GONE
                binding.emptyTitleText.text = getString(com.artf.popularmovies.R.string.no_favorite)
                binding.emptySubtitleText.text = getString(com.artf.popularmovies.R.string.no_favorite_sub_text)
            }
            ApiStatus.DONE -> {
                binding.emptyView.visibility = View.GONE
                binding.loadingIndicator.visibility = View.GONE
            }
            ApiStatus.CONNECTION_ERROR -> {
                binding.emptyView.visibility = View.VISIBLE
                binding.loadingIndicator.visibility = View.GONE
                binding.emptyTitleText.text = getString(com.artf.popularmovies.R.string.server_problem)
                binding.emptySubtitleText.text = getString(com.artf.popularmovies.R.string.server_problem_sub_text)
            }
        }
    }
}