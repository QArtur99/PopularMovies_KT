package com.qartf.popularmovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qartf.popularmovies.gridView.GridViewViewModel
import com.qartf.popularmovies.model.Result
import com.qartf.popularmovies.movieDetail.MovieDetailViewModel
import com.qartf.popularmovies.repository.Repository

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val tasksRepository: Repository,
    private val prefResult: Result? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(GridViewViewModel::class.java) -> GridViewViewModel(tasksRepository, prefResult!!)
                isAssignableFrom(MovieDetailViewModel::class.java) -> MovieDetailViewModel(tasksRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
