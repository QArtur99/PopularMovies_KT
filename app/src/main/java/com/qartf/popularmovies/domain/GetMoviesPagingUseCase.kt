package com.qartf.popularmovies.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.qartf.popularmovies.data.model.DiscoverMovie
import com.qartf.popularmovies.data.model.Listing
import com.qartf.popularmovies.data.model.Movie
import com.qartf.popularmovies.data.repository.Repository
import com.qartf.popularmovies.utility.Constants

interface GetMoviesPagingUseCase {
    operator fun invoke(discoverMovie: LiveData<DiscoverMovie>): LiveData<Listing<Movie>>
}

class GetMoviesPagingUseCaseImpl(private val repository: Repository) : GetMoviesPagingUseCase {
    override fun invoke(discoverMovie: LiveData<DiscoverMovie>): LiveData<Listing<Movie>> {
        return Transformations.map(discoverMovie) {
            when (it.sortBy) {
                Constants.SORT_BY_FAVORITE -> repository.getMoviesPagingDB(it.sortBy, 20)
                else -> repository.getMoviesPaging(it.sortBy, it.sortByGenre)
            }
        }
    }
}