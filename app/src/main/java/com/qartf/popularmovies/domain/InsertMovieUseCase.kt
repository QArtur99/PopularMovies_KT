package com.qartf.popularmovies.domain

import com.qartf.popularmovies.data.database.MovieItem
import com.qartf.popularmovies.data.repository.Repository

interface InsertMovieUseCase {
    suspend operator fun invoke(movieItem: MovieItem)
}

class InsertMovieUseCaseImpl(private val repository: Repository) : InsertMovieUseCase {
    override suspend fun invoke(movieItem: MovieItem) {
        repository.insert(movieItem)
    }
}