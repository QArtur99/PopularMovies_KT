package com.qartf.popularmovies.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.qartf.popularmovies.domain.Movie
import com.qartf.popularmovies.network.TheMovieDbApi
import java.util.*
import java.util.concurrent.Executor

class MovieDataSourceFactory(
    private val theMovieDbApi: TheMovieDbApi,
    private val args: HashMap<String, String>,
    private val retryExecutor: Executor
) : DataSource.Factory<String, Movie>() {

    val sourceLiveData = MutableLiveData<PageKeyedDataSource>()

    override fun create(): DataSource<String, Movie> {
        val source = PageKeyedDataSource(theMovieDbApi, args, retryExecutor)
        sourceLiveData.postValue(source)
        return source
    }
}