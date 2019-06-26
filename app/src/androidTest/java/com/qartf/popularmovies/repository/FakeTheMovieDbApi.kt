package com.qartf.popularmovies.repository

import com.qartf.popularmovies.domain.Movie
import com.qartf.popularmovies.domain.MovieContainer
import com.qartf.popularmovies.domain.ReviewContainer
import com.qartf.popularmovies.domain.VideoContainer
import com.qartf.popularmovies.network.TheMovieDbApi
import com.qartf.popularmovies.utility.Constants
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.mock.Calls
import java.io.IOException

/**
 * implements the RedditApi with controllable requests
 */
class FakeTheMovieDbApi : TheMovieDbApi {
    override fun getMovies(sortBy: String, args: Map<String, String>): Call<MovieContainer> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getMovieTrailersAsync(movieId: String, args: Map<String, String>): Deferred<VideoContainer> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getMovieReviewsAsync(movieId: String, args: Map<String, String>): Deferred<ReviewContainer> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getMoviesAsync(sortBy: String, args: Map<String, String>): Deferred<MovieContainer> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    private val model = mutableMapOf<String, SortBy>()
    var failureMsg: String? = null
    var sortByTest: String = ""

    fun addPost(sortBy: String, post: Movie) {
        val movie = model.getOrPut(sortBy) {
            SortBy(items = arrayListOf())
        }
        movie.items.add(post)
    }

    fun clear() {
        model.clear()
    }

    private fun findMovies(sortBy: String) =
        model.getOrDefault(sortBy, SortBy())

    private fun findMovies(
        sortBy: String,
        limit: Int,
        after: String? = null
    ): List<Movie> {
        val sortByMovies = findMovies(sortBy)
        val posts = sortByMovies.findMovies(limit, after)
        return posts.map { it.copy() }
    }

    override fun getDiscoverMovie(args: Map<String, String>): Call<MovieContainer> {
        failureMsg?.let {
            return Calls.failure(IOException(it))
        }
        val sortBy = args[Constants.SORT_BY]
        val after = args[Constants.PAGE] ?: ""
        val items = findMovies(sortByTest, 20, after)

        val response = MovieContainer(
            page = after,
            total_results = "",
            total_pages = "",
            movies = items
        )
        return Calls.response(response)
    }

    private class SortBy(val items: MutableList<Movie> = arrayListOf()) {
        fun findMovies(limit: Int, after: String?): List<Movie> {
            if (after == null) {
                return items.subList(0, Math.min(items.size, limit))
            }
            val index = items.indexOfFirst { 2 > after.toInt() }
            if (index == -1) {
                return emptyList()
            }
            val startPos = index
            return items.subList(startPos, Math.min(items.size, startPos + limit))
        }
    }
}