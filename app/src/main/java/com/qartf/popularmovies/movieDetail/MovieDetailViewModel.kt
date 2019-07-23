package com.qartf.popularmovies.movieDetail

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.qartf.popularmovies.domain.Movie
import com.qartf.popularmovies.domain.Review
import com.qartf.popularmovies.domain.ReviewContainer
import com.qartf.popularmovies.domain.Video
import com.qartf.popularmovies.domain.VideoContainer
import com.qartf.popularmovies.repository.Repository
import com.qartf.popularmovies.utility.Constants.ApiStatus
import com.qartf.popularmovies.utility.Constants.FabStatus
import com.qartf.popularmovies.utility.asDatabaseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieDetailViewModel(private val repository: Repository) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _listItem = MutableLiveData<Movie>()
    val listItem: LiveData<Movie>
        get() = _listItem

    fun setListItem(listItem: Movie) {
        _listItem.value = listItem
        getMovieReviewsAsync()
        getMovieTrailersAsync()
    }

    fun setFirstListItem(listItem: Movie) {
        if (_listItem.value == null) {
            _listItem.value = listItem
            getMovieReviewsAsync()
            getMovieTrailersAsync()
        }
    }

    private val _appBarLayout = MutableLiveData<FabStatus>()
    val appBarLayoutOpen: LiveData<FabStatus>
        get() = _appBarLayout

    fun onAppBarLayoutOpen(open: FabStatus) {
        _appBarLayout.value = open
    }

    private val _poster = MutableLiveData<Drawable>()
    val poster: LiveData<Drawable>
        get() = _poster

    fun setPoster(drawable: Drawable) {
        _poster.value = drawable
    }

    private val _showReviews = MutableLiveData<Boolean>()
    val showReviews: LiveData<Boolean>
        get() = _showReviews

    fun onShowReviews(show: Boolean?) {
        _showReviews.value = show
    }

    private val _showTrailers = MutableLiveData<Boolean>()
    val showTrailers: LiveData<Boolean>
        get() = _showTrailers

    fun onShowTrailers(show: Boolean?) {
        _showTrailers.value = show
    }

    private val _showOverview = MutableLiveData<Boolean>()
    val showOverview: LiveData<Boolean>
        get() = _showOverview

    fun onShowOverview(show: Boolean) {
        _showOverview.value = !(_showOverview.value ?: false)
    }

    private val _reviewListItem = MutableLiveData<Review>()
    val reviewListItem: LiveData<Review>
        get() = _reviewListItem

    fun onReviewListItemClick(listItem: Review) {
        _reviewListItem.value = listItem
    }

    private val _videoListItem = MutableLiveData<Video>()
    val videoListItem: LiveData<Video>
        get() = _videoListItem

    fun onVideoListItemClick(listItem: Video) {
        _videoListItem.value = listItem
    }

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val _reviews = MutableLiveData<ReviewContainer>()
    val reviews: LiveData<ReviewContainer>
        get() = _reviews

    private val _videos = MutableLiveData<VideoContainer>()
    val videos: LiveData<VideoContainer>
        get() = _videos

    private fun getMovieReviewsAsync() {
        uiScope.launch {
            try {
                _status.value = ApiStatus.LOADING
                val listResult = repository.getMovieReviewsAsync(listItem.value!!.id)
                _status.value = ApiStatus.DONE
                _reviews.value = listResult
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _reviews.value = null
            }
        }
    }

    private fun getMovieTrailersAsync() {
        uiScope.launch {
            try {
                _status.value = ApiStatus.LOADING
                val listResult = repository.getMovieTrailersAsync(listItem.value!!.id)
                _status.value = ApiStatus.DONE
                _videos.value = listResult
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _videos.value = null
            }
        }
    }

    val isFavorite = Transformations.switchMap(listItem) { repository.getMovieWithId(it?.id ?: "") }!!

    private val _fabButton = MutableLiveData<Boolean>()
    val fabButton: LiveData<Boolean>
        get() = _fabButton

    fun setFabButton(show: Boolean?) {
        _fabButton.value = show
    }

    fun onFabButtonClick(show: Boolean) {
        if (favorite.value != null) {
            if (favorite.value!!) onDeleteFavorite() else onAddFavorite()
            setFabButton(true)
        }
    }

    private val _favorite = MutableLiveData<Boolean>()
    val favorite: LiveData<Boolean>
        get() = _favorite

    fun setFavorite(open: Boolean) {
        _favorite.value = open
    }

    fun onAddFavorite() {
        uiScope.launch {
            listItem.value?.let {
                repository.insert(it.asDatabaseModel())
            }
        }
    }

    fun onDeleteFavorite() {
        uiScope.launch {
            listItem.value?.let {
                repository.delete(it.asDatabaseModel().id)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}