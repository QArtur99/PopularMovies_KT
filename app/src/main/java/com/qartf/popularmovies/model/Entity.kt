package com.qartf.popularmovies.model

import android.view.View
import com.qartf.popularmovies.domain.Movie

data class Result(val columns: Int, val sortBy: String, val genre: String)
data class ResultMovie(val v: View, val movie: Movie)
data class Genre(val key: String, val value: String)
data class DiscoverMovie(var sortBy: String, var sortByGenre: String)
