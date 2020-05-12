package com.qartf.popularmovies.domain

import com.qartf.popularmovies.data.model.VideoContainer
import com.qartf.popularmovies.data.repository.Repository

interface GetMovieTrailersUseCase {
    suspend operator fun invoke(movieId: String): VideoContainer
}

class GetMovieTrailersUseCaseImpl(private val repository: Repository) : GetMovieTrailersUseCase {
    override suspend fun invoke(movieId: String): VideoContainer {
        return repository.getMovieTrailersAsync(movieId)
    }
}