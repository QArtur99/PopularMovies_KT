package com.qartf.popularmovies.domain.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.qartf.popularmovies.data.model.Movie
import com.qartf.popularmovies.data.network.TheMovieDbApi
import java.util.HashMap
import java.util.concurrent.Executor

class MovieDataSourceFactory(
    private val theMovieDbApi: TheMovieDbApi,
    private val args: HashMap<String, String>,
    private val retryExecutor: Executor
) : DataSource.Factory<String, Movie>() {

    val sourceLiveData = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<String, Movie> {
        val source = MovieDataSource(
            theMovieDbApi,
            args,
            retryExecutor
        )
        sourceLiveData.postValue(source)
        return source
    }
}