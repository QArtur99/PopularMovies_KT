package com.qartf.popularmovies.domain

import com.qartf.popularmovies.data.model.ReviewContainer
import com.qartf.popularmovies.data.repository.Repository

interface GetMovieReviewUseCase {
    suspend operator fun invoke(movieId: String): ReviewContainer
}

class GetMovieReviewUseCaseImpl(private val repository: Repository) : GetMovieReviewUseCase {
    override suspend fun invoke(movieId: String): ReviewContainer {
        return repository.getMovieReviewsAsync(movieId)
    }
}