package com.artf.popularmovies.movieDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artf.popularmovies.database.MovieDatabaseDao

class MovieDetailViewModelFactory(private val movieDatabase: MovieDatabaseDao) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            return MovieDetailViewModel(movieDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}