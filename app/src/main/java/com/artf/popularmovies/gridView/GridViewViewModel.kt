package com.artf.popularmovies.gridView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artf.popularmovies.domain.Movie
import com.artf.popularmovies.domain.MovieContainer
import com.artf.popularmovies.repository.Repository
import com.artf.popularmovies.utility.Constants.ApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GridViewViewModel(private val columnNo: Int, private val sortByInit: String, private val pageNo: String) : ViewModel() {

    private val repository = Repository()
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val _properties = MutableLiveData<MovieContainer>()
    val properties: LiveData<MovieContainer>
        get() = _properties

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
    fun onRecyclerItemClick(listItem: Movie) {
        _listItem.value = listItem
    }

    init {
        onColumnChanged(columnNo)
        getMovies(sortByInit, pageNo)
    }

    private fun getMovies(sortBy: String, pageNo: String) {
        uiScope.launch {
            try {
                _status.value = ApiStatus.LOADING
                val listResult = repository.getMoviesAsync(sortBy, pageNo)
                _status.value = ApiStatus.DONE
                _properties.value = listResult
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _properties.value = null
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}