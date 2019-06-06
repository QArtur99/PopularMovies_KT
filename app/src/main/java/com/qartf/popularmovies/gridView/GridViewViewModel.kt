package com.qartf.popularmovies.gridView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.qartf.popularmovies.domain.Movie
import com.qartf.popularmovies.repository.Repository
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_FAVORITE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class GridViewViewModel(
    private val repository: Repository,
    private val columnNo: Int,
    private val sortByInit: String,
    private val pageNo: String
) :
    ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _columns = MutableLiveData<Int>()
    val columns: LiveData<Int>
        get() = _columns

    fun onColumnChanged(requestId: Int) {
        _columns.value = requestId
    }

    private val _sortBy = MutableLiveData<String>()
    val sortBy: LiveData<String>
        get() = _sortBy

    fun onSortByChanged(requestId: String) {
        _sortBy.value = requestId
    }

    private val _listItem = MutableLiveData<Movie>()
    val listItem: LiveData<Movie>
        get() = _listItem

    fun onRecyclerItemClick(listItem: Movie?) {
        _listItem.value = listItem
    }

    init {
        onColumnChanged(columnNo)
        onSortByChanged(sortByInit)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    //Paging
    private val repoResult = Transformations.map(sortBy) {
        when (it) {
            SORT_BY_FAVORITE -> repository.getMoviesPagingDB(it, 20)
            else -> repository.getMoviesPaging(it, 20)
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