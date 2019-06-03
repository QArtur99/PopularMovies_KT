package com.qartf.popularmovies.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.qartf.popularmovies.domain.Movie
import com.qartf.popularmovies.network.TheMovieDbAPI
import java.util.concurrent.Executor

class MovieDataSourceFactory(
    private val theMovieDbAPI: TheMovieDbAPI,
    private val sortBy: String,
    private val retryExecutor: Executor
) : DataSource.Factory<String, Movie>() {

    val sourceLiveData = MutableLiveData<PageKeyedDataSource>()

    override fun create(): DataSource<String, Movie> {
        val source = PageKeyedDataSource(theMovieDbAPI, sortBy, retryExecutor)
        sourceLiveData.postValue(source)
        return source
    }
}