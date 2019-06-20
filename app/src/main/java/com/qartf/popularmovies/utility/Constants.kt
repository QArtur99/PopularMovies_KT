package com.qartf.popularmovies.utility

import com.qartf.popularmovies.BuildConfig

class Constants {

    companion object {
        val GENRE_LIST_MOVIE = mutableListOf<Genre>().apply {
            add(Genre("", "All Genres"))
            add(Genre("16", "Animation"))
            add(Genre("35", "Comedy"))
            add(Genre("80", "Crime"))
            add(Genre("99", "Documentary"))
            add(Genre("18", "Drama"))
            add(Genre("10751", "Family"))
            add(Genre("9648", "Mystery"))
            add(Genre("37", "Western"))
        }

        val GENRE_LIST_TV = mutableListOf<Genre>().apply {
            add(Genre("", "All Genres"))
            add(Genre("10759", "Action & Adventure"))
            add(Genre("16", "Animation"))
            add(Genre("35", "Comedy"))
            add(Genre("80", "Crime"))
            add(Genre("99", "Documentary"))
            add(Genre("18", "Drama"))
            add(Genre("10751", "Family"))
            add(Genre("10762", "Kids"))
            add(Genre("9648", "Mystery"))
            add(Genre("10763", "News"))
            add(Genre("10764", "Reality"))
            add(Genre("10765", "Sci-Fi & Fantasy"))
            add(Genre("10766", "Soap"))
            add(Genre("10767", "Talk"))
            add(Genre("10768", "War & Politics"))
            add(Genre("37", "Western"))
        }

        val GENRE_MAP = HashMap<String, String>().apply {
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

        const val SORT_BY_GENRE_KEY = "genreKey"
        const val SORT_BY_GENRE_DEFAULT = ""

        const val NUMBER_OF_COLUMNS_KEY = "show_x_columns"
        const val NUMBER_OF_COLUMNS_DEFAULT = 2

        const val SORT_BY_KEY = "sort_by"
        const val SORT_BY_FAVORITE = "favorite"
        const val SORT_BY_POPULARITY = "popularity.desc"
        const val SORT_BY_RELEASE_DATE = "release_date.desc"
        const val SORT_BY_REVENUE = "revenue.desc"
        const val SORT_BY_VOTE_AVERAGE = "vote_average.desc"
        const val SORT_BY_VOTE_COUNT = "vote_count.desc"

        const val API_KEY = "api_key"
        const val SORT_BY = "sort_by"
        const val WITH_GENRES = "with_genres"
        const val PAGE = "page"
        const val VIDEOS = "videos"
        const val REVIEWS = "reviews"

        const val THE_MOVIE_DB_API_TOKEN = BuildConfig.THE_MOVIE_DB_API_TOKEN
        const val THE_MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3"

        const val TOOLBAR_IMAGE = "toolbarImage"
    }

    enum class ApiStatus { LOADING, ERROR, DONE, CONNECTION_ERROR }
    enum class FabStatus { TOP, BOTTOM, NONE }
}