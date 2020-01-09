package com.qartf.popularmovies.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qartf.popularmovies.ui.gridView.GridViewViewModel
import com.qartf.popularmovies.data.model.Result
import com.qartf.popularmovies.ui.movieDetail.MovieDetailViewModel
import com.qartf.popularmovies.domain.repository.Repository

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
                isAssignableFrom(GridViewViewModel::class.java) -> GridViewViewModel(
                    tasksRepository,
                    prefResult!!
                )
                isAssignableFrom(MovieDetailViewModel::class.java) -> MovieDetailViewModel(
                    tasksRepository
                )
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
