package com.qartf.popularmovies.utility

import com.qartf.popularmovies.BuildConfig

class Constants {

    companion object {
        val GENRES_MAP = HashMap<String, String>().apply {
            put("10759", "Action & Adventure")
            put("16", "Animation")
            put("35", "Comedy")
            put("80", "Crime")
            put("99", "Documentary")
            put("18", "Drama")
            put("10751", "Family")
            put("10762", "Kids")
            put("9648", "Mystery")
            put("10763", "News")
            put("10764", "Reality")
            put("10765", "Sci-Fi & Fantasy")
            put("10766", "Soap")
            put("10767", "Talk")
            put("10768", "War & Politics")
            put("37", "Western")
        }

        const val RECYCLER_VIEW_STATE_ID = "recyclerViewStateId"
        const val INTENT_LIST_ITEM_ID = "listItem"

        const val SORT_BY_FAVORITE = "favorite"
        const val SORT_BY_MOST_POPULAR = "popular"
        const val SORT_BY_TOP_RATED = "top_rated"

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