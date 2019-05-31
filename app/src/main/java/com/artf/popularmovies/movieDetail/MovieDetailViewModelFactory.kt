package com.artf.popularmovies.movieDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artf.popularmovies.domain.Movie

class MovieDetailViewModelFactory :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            return MovieDetailViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}