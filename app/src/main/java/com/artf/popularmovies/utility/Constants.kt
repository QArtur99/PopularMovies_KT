package com.artf.popularmovies.utility

import com.artf.popularmovies.BuildConfig

class Constants {

    companion object {
        const val RECYCLER_VIEW_STATE_ID = "recyclerViewStateId"

        const val INTENT_LIST_ITEM = "listItem"
        const val SORT_BY_FAVORITE = "favorite"

        const val API_KEY = "api_key"
        const val PAGE = "page"
        const val VIDEOS = "videos"
        const val REVIEWS = "reviews"

        const val THE_MOVIE_DB_API_TOKEN = BuildConfig.THE_MOVIE_DB_API_TOKEN
        const val THE_MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/movie/"
    }

    enum class ApiStatus { LOADING, ERROR, DONE, CONNECTION_ERROR }
    data class Result(val columns: Int, val sortBy: String)
}