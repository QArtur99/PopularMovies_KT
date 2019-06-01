package com.artf.popularmovies.movieDetail

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artf.popularmovies.database.MovieDatabaseDao
import com.artf.popularmovies.database.MovieItem
import com.artf.popularmovies.domain.*
import com.artf.popularmovies.repository.Repository
import com.artf.popularmovies.repository.asDatabaseModel
import com.artf.popularmovies.utility.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieDetailViewModel(movieDatabase: MovieDatabaseDao) : ViewModel() {

    private val repository = Repository(movieDatabase)
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

    private val _appBarLayout = MutableLiveData<Boolean>()
    val appBarLayoutOpen: LiveData<Boolean>
        get() = _appBarLayout

    fun onAppBarLayoutOpen(open: Boolean) {
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

    fun onShowReviews(show: Boolean) {
        _showReviews.value = show
    }

    private val _showTrailers = MutableLiveData<Boolean>()
    val showTrailers: LiveData<Boolean>
        get() = _showTrailers

    fun onShowTrailers(show: Boolean) {
        _showTrailers.value = show
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

    private val _status = MutableLiveData<Constants.ApiStatus>()
    val status: LiveData<Constants.ApiStatus>
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
                _status.value = Constants.ApiStatus.LOADING
                val listResult = repository.getMovieReviewsAsync(listItem.value!!.id)
                _status.value = Constants.ApiStatus.DONE
                _reviews.value = listResult
            } catch (e: Exception) {
                _status.value = Constants.ApiStatus.ERROR
                _reviews.value = null
            }
        }
    }

    private fun getMovieTrailersAsync() {
        uiScope.launch {
            try {
                _status.value = Constants.ApiStatus.LOADING
                val listResult = repository.getMovieTrailersAsync(listItem.value!!.id)
                _status.value = Constants.ApiStatus.DONE
                _videos.value = listResult
            } catch (e: Exception) {
                _status.value = Constants.ApiStatus.ERROR
                _videos.value = null
            }
        }
    }


    val isFavorite: LiveData<MovieItem?>
        get() = listItem.value.let {repository.getMovieWithId(it?.id ?: "")}

    private val _fabButton = MutableLiveData<Boolean>()
    val fabButton: LiveData<Boolean>
        get() = _fabButton

    fun onFabButtonClick(show: Boolean) {
        if (favorite.value!!) onDeleteFavorite() else onAddFavorite()
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