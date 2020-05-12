package com.qartf.popularmovies.domain

import com.qartf.popularmovies.data.repository.Repository

interface DeleteMovieUseCase {
    suspend operator fun invoke(movieItemId: String)
}

class DeleteMovieUseCaseImpl(private val repository: Repository) : DeleteMovieUseCase {
    override suspend fun invoke(movieItemId: String) {
        repository.delete(movieItemId)
    }
}