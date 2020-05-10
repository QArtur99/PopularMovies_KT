package com.qartf.popularmovies.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.qartf.popularmovies.domain.NetworkState
import com.qartf.popularmovies.data.model.Movie
import com.qartf.popularmovies.data.model.MovieContainer
import com.qartf.popularmovies.data.network.TheMovieDbApi
import com.qartf.popularmovies.utility.Constants
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.HashMap
import java.util.concurrent.Executor

class MovieDataSource(
    private val theMovieDbApi: TheMovieDbApi,
    private val args: HashMap<String, String>,
    private val retryExecutor: Executor
) : PageKeyedDataSource<String, Movie>() {

    private var retry: (() -> Any)? = null
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

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Movie>) {}

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Movie>) {
        networkState.postValue(NetworkState.LOADING)

        args[Constants.PAGE] = params.key
        theMovieDbApi.getDiscoverMovie(args).enqueue(
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
                        callback.onResult(items, (data?.page?.toInt()?.plus(1)).toString())
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

        args[Constants.PAGE] = "1"
        val request = theMovieDbApi.getDiscoverMovie(args)

        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        try {
            val response = request.execute()
            val data = response.body()
            val items = data?.movies ?: emptyList()
            retry = null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            callback.onResult(items, "", (data?.page?.toInt()?.plus(1)).toString())
        } catch (ioException: IOException) {
            retry = { loadInitial(params, callback) }
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
        }
    }
}