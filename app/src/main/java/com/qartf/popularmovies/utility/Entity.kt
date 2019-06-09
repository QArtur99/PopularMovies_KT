package com.qartf.popularmovies.utility

data class Result(val columns: Int, val sortBy: String, val genre: String)
data class Genre(val key: String, val value: String)
data class DiscoverMovie(var sortBy: String, var sortByGenre: String)
