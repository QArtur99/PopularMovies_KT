package com.qartf.popularmovies.ui

import android.animation.ValueAnimator
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.AppBarLayout
import com.qartf.popularmovies.R
import com.qartf.popularmovies.databinding.ActivityMovieDetailBinding
import com.qartf.popularmovies.domain.mapper.convertFromString
import com.qartf.popularmovies.ui.movieDetail.MovieDetailViewModel
import com.qartf.popularmovies.utility.Constants.Companion.INTENT_LIST_ITEM_ID
import com.qartf.popularmovies.utility.Constants.Companion.TOOLBAR_IMAGE
import com.qartf.popularmovies.utility.Constants.FabStatus
import kotlinx.android.synthetic.main.activity_movie_detail.*
import org.koin.android.ext.android.inject
import kotlin.math.abs

class MovieDetailActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    private val movieDetailViewModel: MovieDetailViewModel by inject()
    private lateinit var binding: ActivityMovieDetailBinding
    private var enterAnimationCompleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.finish()
            return
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        binding.fabTop.visibility = View.GONE
        binding.fabBottom.visibility = View.GONE

        binding.lifecycleOwner = this
        binding.movieDetailViewModel = movieDetailViewModel
        binding.appBar.addOnOffsetChangedListener(this)
        intent.getStringExtra(INTENT_LIST_ITEM_ID)?.let {
            movieDetailViewModel.setListItem(convertFromString(it))
        }
        movieDetailViewModel.onAppBarLayoutOpen(FabStatus.NONE)

        binding.toolbarImage.transitionName = TOOLBAR_IMAGE
        binding.root.post {
            nestedScrollView.isActivated = true
            onEnterAnimationSlide()
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (enterAnimationCompleted) {
            val closePercentage = abs(verticalOffset).toDouble() / collapsingToolbar.height
            val fabStatus = if (closePercentage > 0.8) FabStatus.BOTTOM else FabStatus.TOP
            movieDetailViewModel.onAppBarLayoutOpen(fabStatus)
        }
    }

    private fun onEnterAnimationSlide() {
        val params = appBar.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as AppBarLayout.Behavior?
        if (behavior != null) {
            val valueAnimator = ValueAnimator.ofInt()
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.addUpdateListener { animation ->
                behavior.topAndBottomOffset = animation.animatedValue as Int
                appBar.requestLayout()
            }
            valueAnimator.setIntValues(0, -600)
            valueAnimator.duration = 800
            valueAnimator.startDelay = 600
            valueAnimator.start()

            valueAnimator.doOnEnd { enterAnimationCompleted = true }
            valueAnimator.doOnCancel { enterAnimationCompleted = true }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        movieDetailViewModel.onAppBarLayoutOpen(FabStatus.NONE)
        super.onBackPressed()
    }
}