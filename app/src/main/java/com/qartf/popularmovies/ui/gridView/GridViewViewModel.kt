package com.qartf.popularmovies.ui.gridView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.qartf.popularmovies.data.model.DiscoverMovie
import com.qartf.popularmovies.data.model.Result
import com.qartf.popularmovies.data.model.ResultMovie
import com.qartf.popularmovies.domain.repository.Repository
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_FAVORITE

class GridViewViewModel(
    private val repository: Repository,
    private val prefResult: Result
) : ViewModel() {

    private val _columns = MutableLiveData<Int>()
    val columns: LiveData<Int> = _columns

    private val _sortBy = MutableLiveData<String>()

    private val _sortByGenre = MutableLiveData<String>()

    private val _listItem = MutableLiveData<ResultMovie>()
    val listItem: LiveData<ResultMovie> = _listItem

    val discoverMovie = MediatorLiveData<DiscoverMovie>().apply {
        addSource(_sortBy) { it?.let { this.value?.sortBy = it; value = value } }
        addSource(_sortByGenre) { it?.let { this.value?.sortByGenre = it; value = value } }
    }

    fun onColumnChanged(requestId: Int) {
        _columns.value = requestId
    }

    fun onSortByChanged(requestId: String) {
        _sortBy.value = requestId
    }

    fun onSortByGenreChanged(sortByGenre: String) {
        _sortByGenre.value = sortByGenre
    }

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