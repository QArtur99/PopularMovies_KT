package com.artf.popularmovies.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.artf.popularmovies.domain.Movie
import com.artf.popularmovies.network.TheMovieDbAPI
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