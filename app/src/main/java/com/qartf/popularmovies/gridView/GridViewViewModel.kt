package com.qartf.popularmovies.gridView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.qartf.popularmovies.model.DiscoverMovie
import com.qartf.popularmovies.model.Result
import com.qartf.popularmovies.model.ResultMovie
import com.qartf.popularmovies.repository.Repository
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_FAVORITE

class GridViewViewModel(
    private val repository: Repository,
    private val prefResult: Result
) : ViewModel() {

    private val _columns = MutableLiveData<Int>()
    val columns: LiveData<Int> = _columns
    fun onColumnChanged(requestId: Int) {
        _columns.value = requestId
    }

    private val _sortBy = MutableLiveData<String>()
    fun onSortByChanged(requestId: String) {
        _sortBy.value = requestId
    }

    private val _sortByGenre = MutableLiveData<String>()
    fun onSortByGenreChanged(sortByGenre: String) {
        _sortByGenre.value = sortByGenre
    }

    val discoverMovie = MediatorLiveData<DiscoverMovie>().apply {
        addSource(_sortBy) {
            it?.let { sourceValue -> this.value?.sortBy = sourceValue }.also { this.value = this.value }
        }
        addSource(_sortByGenre) {
            it?.let { sourceValue -> this.value?.sortByGenre = sourceValue }.also { this.value = this.value }
        }
    }

    private val _listItem = MutableLiveData<ResultMovie>()
    val listItem: LiveData<ResultMovie> = _listItem
    fun onRecyclerItemClick(listItem: ResultMovie?) {
        _listItem.value = listItem
    }

    init {
        val (columns, sortBy, genre) = prefResult
        onColumnChanged(columns)
        discoverMovie.value = DiscoverMovie(sortBy, genre)
    }

    // Paging
    private val repoResult = Transformations.map(discoverMovie) {
        when (it.sortBy) {
            SORT_BY_FAVORITE -> repository.getMoviesPagingDB(it.sortBy, 20)
            else -> repository.getMoviesPaging(it.sortBy, it.sortByGenre)
        }
    }

    val posts = Transformations.switchMap(repoResult) { it.pagedList }
    val networkState = Transformations.switchMap(repoResult) { it.networkState }
    val refreshState = Transformations.switchMap(repoResult) { it.refreshState }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = repoResult.value
        listing?.retry?.invoke()
    }
}