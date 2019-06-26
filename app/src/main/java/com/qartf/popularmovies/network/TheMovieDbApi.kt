package com.qartf.popularmovies.network

import com.qartf.popularmovies.domain.MovieContainer
import com.qartf.popularmovies.domain.ReviewContainer
import com.qartf.popularmovies.domain.VideoContainer
import com.qartf.popularmovies.utility.Constants.Companion.THE_MOVIE_DB_BASE_URL
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface TheMovieDbApi {

    @GET("$THE_MOVIE_DB_BASE_URL/movie/{sortBy}")
    fun getMovies(
        @Path(value = "sortBy", encoded = true) sortBy: String,
        @QueryMap args: Map<String, String>
    ): Call<MovieContainer>

    @GET("$THE_MOVIE_DB_BASE_URL/discover/movie")
    fun getDiscoverMovie(
        @QueryMap args: Map<String, String>
    ): Call<MovieContainer>

    @GET("$THE_MOVIE_DB_BASE_URL/movie/{sortBy}")
    fun getMoviesAsync(
        @Path(value = "sortBy", encoded = true) sortBy: String,
        @QueryMap args: Map<String, String>
    ): Deferred<MovieContainer>

    @GET("$THE_MOVIE_DB_BASE_URL/movie/{movieId}/reviews")
    fun getMovieReviewsAsync(
        @Path(value = "movieId", encoded = true) movieId: String,
        @QueryMap args: Map<String, String>
    ): Deferred<ReviewContainer>

    @GET("$THE_MOVIE_DB_BASE_URL/movie/{movieId}/videos")
    fun getMovieTrailersAsync(
        @Path(value = "movieId", encoded = true) movieId: String,
        @QueryMap args: Map<String, String>
    ): Deferred<VideoContainer>
}