package com.qartf.popularmovies.di

import android.content.Context
import com.qartf.popularmovies.data.database.MovieDatabase
import com.qartf.popularmovies.data.database.MovieDatabaseDao
import com.qartf.popularmovies.data.network.RetrofitModule
import com.qartf.popularmovies.data.network.TheMovieDbApi
import com.qartf.popularmovies.data.repository.Repository
import com.qartf.popularmovies.data.repository.RepositoryImpl
import org.koin.dsl.module
import java.util.concurrent.Executor
import java.util.concurrent.Executors

val dataModule = module {
    single { createRepository(get(), get(), get(), get()) }
    single { createMovieDb(get()) }
    single { createMovieDbApi() }
    single { createNetworkExecutor() }
}

fun createNetworkExecutor(): Executor = Executors.newFixedThreadPool(5)

// fun createDiskIOExecutor(): Executor = Executors.newSingleThreadExecutor()

fun createMovieDb(context: Context): MovieDatabaseDao =
    MovieDatabase.getInstance(context).movieDatabaseDao()

fun createMovieDbApi(): TheMovieDbApi = RetrofitModule.devbytes

fun createRepository(
    productDatabase: MovieDatabaseDao,
    api: TheMovieDbApi? = null,
    diskExecutor: Executor,
    networkExecutor: Executor
): Repository {
    return RepositoryImpl(productDatabase, api, diskExecutor, networkExecutor)
}