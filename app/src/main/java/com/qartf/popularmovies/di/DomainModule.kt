package com.qartf.popularmovies.di

import com.qartf.popularmovies.data.repository.Repository
import com.qartf.popularmovies.domain.DeleteMovieUseCase
import com.qartf.popularmovies.domain.DeleteMovieUseCaseImpl
import com.qartf.popularmovies.domain.GetMovieReviewUseCase
import com.qartf.popularmovies.domain.GetMovieReviewUseCaseImpl
import com.qartf.popularmovies.domain.GetMovieTrailersUseCase
import com.qartf.popularmovies.domain.GetMovieTrailersUseCaseImpl
import com.qartf.popularmovies.domain.GetMovieWithIdUseCase
import com.qartf.popularmovies.domain.GetMovieWithIdUseCaseImpl
import com.qartf.popularmovies.domain.GetMoviesPagingUseCase
import com.qartf.popularmovies.domain.GetMoviesPagingUseCaseImpl
import com.qartf.popularmovies.domain.InsertMovieUseCase
import com.qartf.popularmovies.domain.InsertMovieUseCaseImpl
import org.koin.dsl.module

val domainModule = module {
    single { createDeleteMovieUseCase(get()) }
    single { createGetMovieReviewUseCase(get()) }
    single { createGetMoviesPagingUseCase(get()) }
    single { createGetMovieTrailersUseCase(get()) }
    single { createGetMovieWithIdUseCase(get()) }
    single { createInsertMovieUseCase(get()) }
}

fun createDeleteMovieUseCase(repository: Repository): DeleteMovieUseCase =
    DeleteMovieUseCaseImpl(repository)

fun createGetMovieReviewUseCase(repository: Repository): GetMovieReviewUseCase =
    GetMovieReviewUseCaseImpl(repository)

fun createGetMoviesPagingUseCase(repository: Repository): GetMoviesPagingUseCase =
    GetMoviesPagingUseCaseImpl(repository)

fun createGetMovieTrailersUseCase(repository: Repository): GetMovieTrailersUseCase =
    GetMovieTrailersUseCaseImpl(repository)

fun createGetMovieWithIdUseCase(repository: Repository): GetMovieWithIdUseCase =
    GetMovieWithIdUseCaseImpl(repository)

fun createInsertMovieUseCase(repository: Repository): InsertMovieUseCase =
    InsertMovieUseCaseImpl(repository)