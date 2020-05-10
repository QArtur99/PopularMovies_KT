package com.qartf.popularmovies.data.repository

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.qartf.popularmovies.data.database.MovieDatabaseDao
import com.qartf.popularmovies.data.database.MovieItem
import com.qartf.popularmovies.domain.Listing
import com.qartf.popularmovies.domain.NetworkState
import com.qartf.popularmovies.data.model.Movie
import com.qartf.popularmovies.data.model.MovieContainer
import com.qartf.popularmovies.data.model.ReviewContainer
import com.qartf.popularmovies.data.model.VideoContainer
import com.qartf.popularmovies.data.network.RetrofitModule
import com.qartf.popularmovies.data.network.TheMovieDbApi
import com.qartf.popularmovies.utility.Constants.Companion.API_KEY
import com.qartf.popularmovies.utility.Constants.Companion.PAGE
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY
import com.qartf.popularmovies.utility.Constants.Companion.THE_MOVIE_DB_API_TOKEN
import com.qartf.popularmovies.utility.Constants.Companion.WITH_GENRES
import com.qartf.popularmovies.domain.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.Executor

class Repository(
    private val productDatabase: MovieDatabaseDao,
    private val api: TheMovieDbApi? = null,
    private val diskExecutor: Executor,
    private val networkExecutor: Executor

) {
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
    fun getMoviesPaging(sortBy: String, genre: String): Listing<Movie> {
        val args = HashMap<String, String>()
        args[API_KEY] = THE_MOVIE_DB_API_TOKEN
        args[SORT_BY] = sortBy
        args[WITH_GENRES] = genre
        args[PAGE] = "1"

        val sourceFactory = MovieDataSourceFactory(
            api!!,
            args,
            networkExecutor
        )

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
        val ds = productDatabase.getAllMoviesDS().map { it.asDomainModel() }

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