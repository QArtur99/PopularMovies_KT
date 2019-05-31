package com.artf.popularmovies.repository

import com.artf.popularmovies.domain.MovieContainer
import com.artf.popularmovies.domain.ReviewContainer
import com.artf.popularmovies.domain.VideoContainer
import com.artf.popularmovies.network.RetrofitModule
import com.artf.popularmovies.utility.Constants.Companion.API_KEY
import com.artf.popularmovies.utility.Constants.Companion.PAGE
import com.artf.popularmovies.utility.Constants.Companion.THE_MOVIE_DB_API_TOKEN
import java.util.*

class Repository(){
    suspend fun getMoviesAsync(sortBy : String, pageNo : String): MovieContainer {
            val args = HashMap<String, String>()
            args[API_KEY] = THE_MOVIE_DB_API_TOKEN
            args[PAGE] = pageNo

        return RetrofitModule.devbytes.getMoviesAsync(sortBy, args).await()
    }

    suspend fun getMovieReviewsAsync(movieId : String): ReviewContainer {
        val args = HashMap<String, String>()
        args[API_KEY] = THE_MOVIE_DB_API_TOKEN

        return RetrofitModule.devbytes.getMovieReviewsAsync(movieId, args).await()
    }

    suspend fun getMovieTrailersAsync(movieId : String): VideoContainer {
        val args = HashMap<String, String>()
        args[API_KEY] = THE_MOVIE_DB_API_TOKEN

        return RetrofitModule.devbytes.getMovieTrailersAsync(movieId, args).await()
    }
}