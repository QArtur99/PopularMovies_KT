package com.artf.popularmovies.gridView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GridViewViewModelFactory(private val columnNo: Int, private val sortByInit: String, private val pageNo: String) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GridViewViewModel::class.java)) {
            return GridViewViewModel(columnNo, sortByInit, pageNo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}