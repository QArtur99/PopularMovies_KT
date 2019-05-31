package com.artf.popularmovies.network

import com.artf.popularmovies.domain.MovieContainer
import com.artf.popularmovies.domain.ReviewContainer
import com.artf.popularmovies.domain.VideoContainer
import com.artf.popularmovies.utility.Constants.Companion.THE_MOVIE_DB_BASE_URL
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface TheMovieDbAPI {

    @GET("$THE_MOVIE_DB_BASE_URL{sortBy}")
    fun getMoviesAsync(
        @Path(value = "sortBy", encoded = true) sortBy: String,
        @QueryMap args: Map<String, String>
    ): Deferred<MovieContainer>

    @GET("$THE_MOVIE_DB_BASE_URL{movieId}/reviews")
    fun getMovieReviewsAsync(
        @Path(value = "movieId", encoded = true) movieId: String,
        @QueryMap args: Map<String, String>
    ): Deferred<ReviewContainer>

    @GET("$THE_MOVIE_DB_BASE_URL{movieId}/videos")
    fun getMovieTrailersAsync(
        @Path(value = "movieId", encoded = true) movieId: String,
        @QueryMap args: Map<String, String>
    ): Deferred<VideoContainer>

}