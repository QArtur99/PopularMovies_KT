package com.qartf.popularmovies.domain.mapper

import com.qartf.popularmovies.data.database.MovieItem
import com.qartf.popularmovies.data.model.Movie
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

fun List<MovieItem>.asDomainModel(): List<Movie> {
    return map { it.asDomainModel() }
}

fun MovieItem.asDomainModel(): Movie {
    return Movie(
        vote_count = vote_count,
        id = id,
        video = video,
        vote_average = vote_average,
        title = title,
        popularity = popularity,
        poster_path = poster_path,
        original_language = original_language,
        original_title = original_title,
        genre_ids = convertFromString(
            genre_ids
        ),
        backdrop_path = backdrop_path,
        adult = adult,
        overview = overview,
        release_date = release_date
    )
}

fun List<Movie>.asDatabaseModel(): List<MovieItem> {
    return map { it.asDatabaseModel() }
}

fun Movie.asDatabaseModel(): MovieItem {
    return MovieItem(
        vote_count = vote_count,
        id = id,
        video = video,
        vote_average = vote_average,
        title = title,
        popularity = popularity,
        poster_path = poster_path ?: "",
        original_language = original_language,
        original_title = original_title,
        genre_ids = convertToString(
            genre_ids
        ),
        backdrop_path = backdrop_path ?: "",
        adult = adult,
        overview = overview,
        release_date = release_date ?: ""
    )
}

inline fun <reified T> convertToString(item: T): String {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val jsonAdapter = moshi.adapter<T>(T::class.java)
    return jsonAdapter.toJson(item)
}

inline fun <reified T> convertFromString(item: String): T {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val jsonAdapter = moshi.adapter<T>(T::class.java)
    return jsonAdapter.fromJson(item)!!
}