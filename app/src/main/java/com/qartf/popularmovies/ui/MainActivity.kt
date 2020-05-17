package com.qartf.popularmovies.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.google.android.material.tabs.TabLayout
import com.qartf.popularmovies.R
import com.qartf.popularmovies.databinding.ActivityMainBinding
import com.qartf.popularmovies.ui.movieDetail.MovieDetailViewModel
import com.qartf.popularmovies.utility.Constants
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_GENRE_DEFAULT
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_GENRE_KEY
import kotlinx.android.synthetic.main.tool_bar.*
import kotlinx.android.synthetic.main.tool_bar.view.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val movieDetailViewModel: MovieDetailViewModel by inject()
    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(application)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var selectedTab: TabLayout.Tab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )
        binding.lifecycleOwner = this
        binding.movieDetailViewModel = movieDetailViewModel

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
