package com.qartf.popularmovies.data.repository

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import com.qartf.popularmovies.data.database.MovieItem
import com.qartf.popularmovies.data.model.Movie
import com.qartf.popularmovies.data.model.MovieContainer
import com.qartf.popularmovies.data.model.ReviewContainer
import com.qartf.popularmovies.data.model.VideoContainer
import com.qartf.popularmovies.data.model.Listing

interface Repository {
    suspend fun getMoviesAsync(sortBy: String, pageNo: String): MovieContainer

    suspend fun getMovieReviewsAsync(movieId: String): ReviewContainer

    suspend fun getMovieTrailersAsync(movieId: String): VideoContainer

    @MainThread
    fun getMoviesPaging(sortBy: String, genre: String): Listing<Movie>

    @MainThread
    fun getMoviesPagingDB(sortBy: String, pageSize: Int): Listing<Movie>

    fun getMovieWithId(movieItemId: String): LiveData<MovieItem?>

    suspend fun insert(movieItem: MovieItem)

    suspend fun delete(movieItemId: String)
}