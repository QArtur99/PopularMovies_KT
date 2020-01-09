package com.qartf.popularmovies.data.model

import android.view.View

data class Result(val columns: Int, val sortBy: String, val genre: String)
data class ResultMovie(val v: View, val movie: Movie)
data class Genre(val key: String, val value: String)
data class DiscoverMovie(var sortBy: String, var sortByGenre: String)
