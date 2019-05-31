package com.artf.popularmovies.repository

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.artf.popularmovies.domain.Movie
import com.artf.popularmovies.domain.MovieContainer
import com.artf.popularmovies.domain.ReviewContainer
import com.artf.popularmovies.domain.VideoContainer
import com.artf.popularmovies.network.RetrofitModule
import com.artf.popularmovies.utility.Constants.Companion.API_KEY
import com.artf.popularmovies.utility.Constants.Companion.PAGE
import com.artf.popularmovies.utility.Constants.Companion.THE_MOVIE_DB_API_TOKEN
import java.util.*

class Repository() {
    suspend fun getMoviesAsync(sortBy: String, pageNo: String): MovieContainer {
        val args = HashMap<String, String>()
        args[API_KEY] = THE_MOVIE_DB_API_TOKEN
        args[PAGE] = pageNo

        return RetrofitModule.devbytes.getMoviesAsync(sortBy, args).await()
    }

    suspend fun getMovieReviewsAsync(movieId: String): ReviewContainer {
        val args = HashMap<String, String>()
        args[API_KEY] = THE_MOVIE_DB_API_TOKEN

        return RetrofitModule.devbytes.getMovieReviewsAsync(movieId, args).await()
    }

    suspend fun getMovieTrailersAsync(movieId: String): VideoContainer {
        val args = HashMap<String, String>()
        args[API_KEY] = THE_MOVIE_DB_API_TOKEN

        return RetrofitModule.devbytes.getMovieTrailersAsync(movieId, args).await()
    }

    @MainThread
    fun getMoviesPaging(sortBy: String, pageSize: Int): Listing<Movie> {
        val sourceFactory = MovieDataSourceFactory(RetrofitModule.devbytes, sortBy, RetrofitModule.NETWORK_IO)
        val livePagedList = LivePagedListBuilder(sourceFactory, pageSize).build()

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) { it.initialLoad }

        return Listing(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) { it.networkState },
            retry = { sourceFactory.sourceLiveData.value?.retryAllFailed() },
            refresh = { sourceFactory.sourceLiveData.value?.invalidate() },
            refreshState = refreshState
        )
    }
}