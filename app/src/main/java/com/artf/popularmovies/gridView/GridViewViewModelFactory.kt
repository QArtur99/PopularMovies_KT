package com.artf.popularmovies.gridView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artf.popularmovies.database.MovieDatabaseDao

class GridViewViewModelFactory(
    private val movieDatabase: MovieDatabaseDao,
    private val columnNo: Int,
    private val sortByInit: String,
    private val pageNo: String
) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GridViewViewModel::class.java)) {
            return GridViewViewModel(movieDatabase, columnNo, sortByInit, pageNo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}