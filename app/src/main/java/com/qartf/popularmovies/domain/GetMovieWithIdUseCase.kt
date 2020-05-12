package com.qartf.popularmovies.domain

import androidx.lifecycle.LiveData
import com.qartf.popularmovies.data.database.MovieItem
import com.qartf.popularmovies.data.repository.Repository

interface GetMovieWithIdUseCase {
    operator fun invoke(movieItemId: String): LiveData<MovieItem?>
}

class GetMovieWithIdUseCaseImpl(private val repository: Repository) : GetMovieWithIdUseCase {
    override fun invoke(movieItemId: String): LiveData<MovieItem?> {
        return repository.getMovieWithId(movieItemId)
    }
}