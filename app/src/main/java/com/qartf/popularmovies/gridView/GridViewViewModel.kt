package com.qartf.popularmovies.gridView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.qartf.popularmovies.repository.Repository
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_FAVORITE
import com.qartf.popularmovies.utility.DiscoverMovie
import com.qartf.popularmovies.utility.Result
import com.qartf.popularmovies.utility.ResultMovie

class GridViewViewModel(
    private val repository: Repository,
    private val prefResult: Result
) :
    ViewModel() {

    private val _columns = MutableLiveData<Int>()
    val columns: LiveData<Int>
        get() = _columns

    fun onColumnChanged(requestId: Int) {
        _columns.value = requestId
    }

    private val _sortBy = MutableLiveData<DiscoverMovie>()
    val sortBy: LiveData<DiscoverMovie>
        get() = _sortBy

    fun onSortByChanged(requestId: String) {
        _sortBy.value?.sortBy = requestId
        _sortBy.value = _sortBy.value
    }

    fun onSortByGenreChanged(sortByGenre: String) {
        _sortBy.value?.sortByGenre = sortByGenre
        _sortBy.value = _sortBy.value
    }

    private val _listItem = MutableLiveData<ResultMovie>()
    val listItem: LiveData<ResultMovie>
        get() = _listItem

    fun onRecyclerItemClick(listItem: ResultMovie?) {
        _listItem.value = listItem
    }

    init {
        val (columns, sortBy, genre) = prefResult
        onColumnChanged(columns)
        _sortBy.value = DiscoverMovie(sortBy, genre)
    }

    // Paging
    private val repoResult = Transformations.map(sortBy) {
        when (it.sortBy) {
            SORT_BY_FAVORITE -> repository.getMoviesPagingDB(it.sortBy, 20)
            else -> repository.getMoviesPaging(it.sortBy, it.sortByGenre)
        }
    }

    val posts = Transformations.switchMap(repoResult) { it.pagedList }!!
    val networkState = Transformations.switchMap(repoResult) { it.networkState }!!
    val refreshState = Transformations.switchMap(repoResult) { it.refreshState }!!

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = repoResult?.value
        listing?.retry?.invoke()
    }
}