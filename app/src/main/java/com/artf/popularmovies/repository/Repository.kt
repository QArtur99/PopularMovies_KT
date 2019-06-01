package com.artf.popularmovies.repository

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.artf.popularmovies.database.MovieDatabaseDao
import com.artf.popularmovies.database.MovieItem
import com.artf.popularmovies.domain.Movie
import com.artf.popularmovies.domain.MovieContainer
import com.artf.popularmovies.domain.ReviewContainer
import com.artf.popularmovies.domain.VideoContainer
import com.artf.popularmovies.network.RetrofitModule
import com.artf.popularmovies.utility.Constants.Companion.API_KEY
import com.artf.popularmovies.utility.Constants.Companion.PAGE
import com.artf.popularmovies.utility.Constants.Companion.THE_MOVIE_DB_API_TOKEN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class Repository(private val productDatabase: MovieDatabaseDao) {
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

        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()

        val livePagedList = LivePagedListBuilder(sourceFactory, config).build()

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) { it.initialLoad }

        return Listing(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) { it.networkState },
            retry = { sourceFactory.sourceLiveData.value?.retryAllFailed() },
            refresh = { sourceFactory.sourceLiveData.value?.invalidate() },
            refreshState = refreshState
        )
    }

    @MainThread
    fun getMoviesPagingDB(sortBy: String, pageSize: Int): Listing<Movie> {
        val ds = productDatabase.getAllMoviesDS().map{it.asDomainModel()}

        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()

        val livePagedList = LivePagedListBuilder(ds, config).build()

        val refreshTrigger = MutableLiveData<Unit>()
        val networkState = MutableLiveData<NetworkState>()
        networkState.postValue(NetworkState.DATABASE)

        return Listing(
            pagedList = livePagedList,
            networkState = networkState,
            retry = { refreshTrigger.value = null },
            refresh = { networkState.postValue(NetworkState.DATABASE) },
            refreshState = networkState
        )
    }


    suspend fun getMovieWithId2(movieItemId: String): MovieItem? {
        return withContext(Dispatchers.IO) {
            productDatabase.getMovieWithId2(movieItemId)
        }
    }

    fun getMovieWithId(movieItemId: String): LiveData<MovieItem?> {
        return productDatabase.getMovieWithId(movieItemId)
    }

    suspend fun insert(movieItem: MovieItem) {
        withContext(Dispatchers.IO) {
            productDatabase.insert(movieItem)
        }
    }

    suspend fun delete(movieItemId: String) {
        withContext(Dispatchers.IO) {
            productDatabase.deleteMovie(movieItemId)
        }
    }
}