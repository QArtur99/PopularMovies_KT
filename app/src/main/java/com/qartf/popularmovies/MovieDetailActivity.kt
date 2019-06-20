package com.qartf.popularmovies


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
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.qartf.popularmovies.databinding.ActivityMovieDetailBinding
import com.qartf.popularmovies.movieDetail.MovieDetailViewModel
import com.qartf.popularmovies.movieDetail.MovieDetailViewModelFactory
import com.qartf.popularmovies.utility.Constants.Companion.INTENT_LIST_ITEM_ID
import com.qartf.popularmovies.utility.Constants.Companion.TOOLBAR_IMAGE
import com.qartf.popularmovies.utility.Constants.FabStatus
import com.qartf.popularmovies.utility.ServiceLocator
import com.qartf.popularmovies.utility.convertFromString
import kotlinx.android.synthetic.main.activity_movie_detail.*


class MovieDetailActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    private val movieDetailViewModel: MovieDetailViewModel by lazy {
        val application = requireNotNull(this).application
        val repository = ServiceLocator.instance(application).getRepository()
        val viewModelFactory = MovieDetailViewModelFactory(repository)
        ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel::class.java)
    }

    private lateinit var binding: ActivityMovieDetailBinding
    private var enterAnimationComleted = false

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

        movieDetailViewModel.setListItem(convertFromString(intent.getStringExtra(INTENT_LIST_ITEM_ID)))
        movieDetailViewModel.onAppBarLayoutOpen(FabStatus.NONE)

        binding.toolbarImage.transitionName = TOOLBAR_IMAGE
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (enterAnimationComleted) {
            val closePercentage = Math.abs(verticalOffset).toDouble() / collapsingToolbar.height
            if (closePercentage > 0.8) {
                movieDetailViewModel.onAppBarLayoutOpen(FabStatus.BOTTOM)
            } else {
                movieDetailViewModel.onAppBarLayoutOpen(FabStatus.TOP)
            }
        }
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        val params = appBar.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as AppBarLayout.Behavior?
        if (behavior != null) {
            val valueAnimator = ValueAnimator.ofInt()
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.addUpdateListener { animation ->
                behavior.topAndBottomOffset = animation.animatedValue as Int
                appBar.requestLayout()
            }
            valueAnimator.setIntValues(0, 0, -600)
            valueAnimator.duration = 1500
            valueAnimator.start()

            valueAnimator.doOnEnd {
                enterAnimationComleted = true
            }
            valueAnimator.doOnCancel {
                enterAnimationComleted = true
            }
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