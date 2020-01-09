package com.qartf.popularmovies.ui.movieDetail

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qartf.popularmovies.data.model.Movie
import com.qartf.popularmovies.data.model.Review
import com.qartf.popularmovies.data.model.ReviewContainer
import com.qartf.popularmovies.data.model.Video
import com.qartf.popularmovies.data.model.VideoContainer
import com.qartf.popularmovies.domain.repository.Repository
import com.qartf.popularmovies.utility.Constants.ApiStatus
import com.qartf.popularmovies.utility.Constants.FabStatus
import com.qartf.popularmovies.domain.asDatabaseModel
import kotlinx.coroutines.launch

class MovieDetailViewModel(private val repository: Repository) : ViewModel() {

    private val _listItem = MutableLiveData<Movie>()
    val listItem: LiveData<Movie> = _listItem

    private val _appBarLayout = MutableLiveData<FabStatus>()
    val appBarLayoutOpen: LiveData<FabStatus> = _appBarLayout

    private val _poster = MutableLiveData<Drawable>()
    val poster: LiveData<Drawable> = _poster

    private val _showReviews = MutableLiveData<Boolean>()
    val showReviews: LiveData<Boolean> = _showReviews

    private val _showTrailers = MutableLiveData<Boolean>()
    val showTrailers: LiveData<Boolean> = _showTrailers

    private val _showOverview = MutableLiveData<Boolean>()
    val showOverview: LiveData<Boolean> = _showOverview

    private val _reviewListItem = MutableLiveData<Review>()
    val reviewListItem: LiveData<Review> = _reviewListItem

    private val _videoListItem = MutableLiveData<Video>()
    val videoListItem: LiveData<Video> = _videoListItem

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> = _status

    private val _reviews = MutableLiveData<ReviewContainer>()
    val reviews: LiveData<ReviewContainer> = _reviews

    private val _videos = MutableLiveData<VideoContainer>()
    val videos: LiveData<VideoContainer> = _videos

    private val _fabButton = MutableLiveData<Boolean>()
    val fabButton: LiveData<Boolean> = _fabButton

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

    fun onAppBarLayoutOpen(open: FabStatus) {
        _appBarLayout.value = open
    }

    fun setPoster(drawable: Drawable) {
        _poster.value = drawable
    }

    fun onShowReviews(show: Boolean?) {
        _showReviews.value = show
    }

    fun onShowTrailers(show: Boolean?) {
        _showTrailers.value = show
    }

    fun onShowOverview(show: Boolean) {
        _showOverview.value = !(_showOverview.value ?: false)
    }

    fun onReviewListItemClick(listItem: Review) {
        _reviewListItem.value = listItem
    }

    fun onVideoListItemClick(listItem: Video) {
        _videoListItem.value = listItem
    }

    private fun getMovieReviewsAsync() {
        viewModelScope.launch {
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
        viewModelScope.launch {
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

    private val isFavorite = Transformations.switchMap(listItem) { repository.getMovieWithId(it?.id ?: "") }
    val favorite = Transformations.map(isFavorite) { it != null }

    fun onFabButtonClick(show: Boolean?) {
        if (show != null)
            if (favorite.value!!) onDeleteFavorite() else onAddFavorite()
        _fabButton.value = show
    }

    private fun onAddFavorite() {
        viewModelScope.launch {
            listItem.value?.let {
                repository.insert(it.asDatabaseModel())
            }
        }
    }

    private fun onDeleteFavorite() {
        viewModelScope.launch {
            listItem.value?.let {
                repository.delete(it.asDatabaseModel().id)
            }
        }
    }
}