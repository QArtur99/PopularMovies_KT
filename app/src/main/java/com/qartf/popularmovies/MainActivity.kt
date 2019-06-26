package com.qartf.popularmovies

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.qartf.popularmovies.databinding.ActivityMainBinding
import com.qartf.popularmovies.movieDetail.MovieDetailViewModel
import com.qartf.popularmovies.movieDetail.MovieDetailViewModelFactory
import com.qartf.popularmovies.utility.Constants
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_GENRE_DEFAULT
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_GENRE_KEY
import com.qartf.popularmovies.utility.ServiceLocator
import kotlinx.android.synthetic.main.tool_bar.*
import kotlinx.android.synthetic.main.tool_bar.view.*

class MainActivity : AppCompatActivity() {

    private val movieDetailViewModel: MovieDetailViewModel by lazy {
        val application = requireNotNull(this).application
        val repository = ServiceLocator.instance(application).getRepository()
        val viewModelFactory = MovieDetailViewModelFactory(repository)
        ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var selectedTab: TabLayout.Tab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.movieDetailViewModel = movieDetailViewModel

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)

        setSupportActionBar(binding.root.toolbar)

        addTabLayoutTabs()
        Handler().postDelayed({
            tabLayout.getTabAt(selectedTab.position)?.select()
            tabListener()
        }, 100)
    }

    private fun addTabLayoutTabs() {
        val sortByGenre = sharedPreferences.getString(SORT_BY_GENRE_KEY, SORT_BY_GENRE_DEFAULT)
        var counter = 0
        Constants.GENRE_LIST_MOVIE.forEach {
            val tab = tabLayout.newTab()
            tab.tag = it.key
            tab.text = it.value
            tabLayout.addTab(tab, counter)
            if (it.key == sortByGenre) {
                selectedTab = tab
            }
            counter = counter.inc()
        }
    }

    private fun tabListener() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                sharedPreferences.edit().putString(SORT_BY_GENRE_KEY, tab.tag as String).apply()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}
