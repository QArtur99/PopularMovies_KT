package com.qartf.popularmovies.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.qartf.popularmovies.domain.Movie
import com.qartf.popularmovies.domain.MovieContainer
import com.qartf.popularmovies.network.TheMovieDbAPI
import com.qartf.popularmovies.utility.Constants
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.*
import java.util.concurrent.Executor

class PageKeyedDataSource(
    private val theMovieDbAPI: TheMovieDbAPI,
    private val sortBy: String,
    private val retryExecutor: Executor
) : PageKeyedDataSource<String, Movie>() {

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, Movie>
    ) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Movie>) {
        networkState.postValue(NetworkState.LOADING)

        val args = HashMap<String, String>()
        args[Constants.API_KEY] = Constants.THE_MOVIE_DB_API_TOKEN
        args[Constants.PAGE] = params.key

        theMovieDbAPI.getMovies(sortBy, args).enqueue(
            object : retrofit2.Callback<MovieContainer> {
                override fun onFailure(call: Call<MovieContainer>, t: Throwable) {
                    retry = { loadAfter(params, callback) }
                    networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
                }

                override fun onResponse(call: Call<MovieContainer>, response: Response<MovieContainer>) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        val items = data?.movies ?: emptyList()
                        retry = null
                        callback.onResult(items, (data?.page?.toInt()?.plus(1) ).toString())
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        retry = { loadAfter(params, callback) }
                        networkState.postValue(NetworkState.error("error code: ${response.code()}"))
                    }
                }
            }
        )
    }

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Movie>) {

        val args = HashMap<String, String>()
        args[Constants.API_KEY] = Constants.THE_MOVIE_DB_API_TOKEN
        args[Constants.PAGE] = "1"

        val request = theMovieDbAPI.getMovies(sortBy, args)

        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        // triggered by a refresh, we better execute sync
        try {
            val response = request.execute()
            val data = response.body()
            val items = data?.movies ?: emptyList()
            retry = null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            callback.onResult(items, "", (data?.page?.toInt()?.plus(1) ).toString())
        } catch (ioException: IOException) {
            retry = { loadInitial(params, callback) }
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
        }
    }
}