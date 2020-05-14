package com.qartf.popularmovies.di

import com.qartf.popularmovies.data.model.Result
import com.qartf.popularmovies.ui.gridView.GridViewViewModel
import com.qartf.popularmovies.ui.movieDetail.MovieDetailViewModel
import com.qartf.popularmovies.utility.Constants
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    single { createResult() }
    single { GridViewViewModel(get(), get()) }
    single { MovieDetailViewModel(get(), get(), get(), get(), get()) }
}

fun createResult() = Result(
    Constants.NUMBER_OF_COLUMNS_DEFAULT,
    Constants.SORT_BY_POPULARITY,
    Constants.SORT_BY_GENRE_DEFAULT
)