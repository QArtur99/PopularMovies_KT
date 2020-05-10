package com.qartf.popularmovies.di

import android.content.Context
import com.qartf.popularmovies.data.database.MovieDatabase
import com.qartf.popularmovies.data.database.MovieDatabaseDao
import com.qartf.popularmovies.data.model.Result
import com.qartf.popularmovies.data.network.RetrofitModule
import com.qartf.popularmovies.data.network.TheMovieDbApi
import com.qartf.popularmovies.data.repository.Repository
import com.qartf.popularmovies.ui.gridView.GridViewViewModel
import com.qartf.popularmovies.ui.movieDetail.MovieDetailViewModel
import com.qartf.popularmovies.utility.Constants
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.Executor
import java.util.concurrent.Executors

val vmModule = module {

    viewModel { GridViewViewModel(get(), get()) }

    viewModel { MovieDetailViewModel(get()) }
}

val dataModule = module {

    single { createRepository(get(), get(), get(), get()) }

    single { createMovieDb(get()) }

    single { createMovieDbApi() }

    single { createNetworkExecutor() }

    single { createResult() }
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
    return Repository(productDatabase, api, diskExecutor, networkExecutor)
}

fun createResult() = Result(
    Constants.NUMBER_OF_COLUMNS_DEFAULT,
    Constants.SORT_BY_POPULARITY,
    Constants.SORT_BY_GENRE_DEFAULT
)