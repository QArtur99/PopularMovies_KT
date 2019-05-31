package com.artf.popularmovies.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass



@JsonClass(generateAdapter = true)
data class MovieContainer(
    val page: String,
    val total_results: String,
    val total_pages: String,
    @Json(name = "results") val movies: List<Movie>)

@JsonClass(generateAdapter = true)
data class Movie(
        val vote_count: String,
        val id: String,
        val video: Boolean,
        val vote_average: String,
        val title: String,
        val popularity: String,
        val poster_path: String,
        val original_language: String,
        val original_title: String,
        val genre_ids: List<String>,
        val backdrop_path: String? = "",
        val adult: Boolean,
        val overview: String,
        val release_date: String?
)