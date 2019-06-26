package com.qartf.popularmovies.utility

import com.qartf.popularmovies.domain.Movie
import java.util.concurrent.atomic.AtomicInteger

class MovieFactory {
    private val counter = AtomicInteger(-1)
    fun createMovie(): Movie {
        val id = counter.incrementAndGet()
        return Movie(
            vote_count = "$id",
            id = "$id",
            video = false,
            vote_average = "$id",
            title = "title$id",
            popularity = "$id",
            poster_path = "",
            original_language = "",
            original_title = "title$id",
            genre_ids = List(0) { "0" },
            backdrop_path = "",
            adult = false,
            overview = "title$id",
            release_date = ""
        )
    }
}