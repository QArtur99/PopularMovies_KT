package com.qartf.popularmovies.gridView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qartf.popularmovies.repository.Repository
import com.qartf.popularmovies.utility.Result

class GridViewViewModelFactory(
    private val repository: Repository,
    private val prefResult: Result
) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GridViewViewModel::class.java)) {
            return GridViewViewModel(repository, prefResult) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}