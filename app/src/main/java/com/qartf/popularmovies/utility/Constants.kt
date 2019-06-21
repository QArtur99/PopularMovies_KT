package com.qartf.popularmovies.utility

import com.qartf.popularmovies.BuildConfig

class Constants {

    companion object {
        val GENRE_LIST_MOVIE = mutableListOf<Genre>().apply {
            add(Genre("", "All Genres"))
            add(Genre("28", "Action"))
            add(Genre("12", "Adventure"))
            add(Genre("16", "Animation"))
            add(Genre("35", "Comedy"))
            add(Genre("80", "Crime"))
            add(Genre("99", "Documentary"))
            add(Genre("18", "Drama"))
            add(Genre("10751", "Family"))
            add(Genre("14", "Fantasy"))
            add(Genre("36", "History"))
            add(Genre("27", "Horror"))
            add(Genre("10402", "Music"))
            add(Genre("9648", "Mystery"))
            add(Genre("10749", "Romance"))
            add(Genre("878", "Science Fiction"))
            add(Genre("10770", "TV Movie"))
            add(Genre("53", "Thriller"))
            add(Genre("10752", "War"))
            add(Genre("37", "Western"))
        }

        val GENRE_LIST_TV = mutableListOf<Genre>().apply {
            add(Genre("", "All Genres"))
            add(Genre("28", "Action"))
            add(Genre("12", "Adventure"))
            add(Genre("16", "Animation"))
            add(Genre("35", "Comedy"))
            add(Genre("80", "Crime"))
            add(Genre("99", "Documentary"))
            add(Genre("18", "Drama"))
            add(Genre("10751", "Family"))
            add(Genre("14", "Fantasy"))
            add(Genre("36", "History"))
            add(Genre("27", "Horror"))
            add(Genre("10402", "Music"))
            add(Genre("9648", "Mystery"))
            add(Genre("10749", "Romance"))
            add(Genre("878", "Science Fiction"))
            add(Genre("10770", "TV Movie"))
            add(Genre("53", "Thriller"))
            add(Genre("10752", "War"))
            add(Genre("37", "Western"))
        }

        val GENRE_MAP = HashMap<String, String>().apply {
            put("28", "Action")
            put("12", "Adventure")
            put("16", "Animation")
            put("35", "Comedy")
            put("80", "Crime")
            put("99", "Documentary")
            put("18", "Drama")
            put("10751", "Family")
            put("14", "Fantasy")
            put("36", "History")
            put("27", "Horror")
            put("10402", "Music")
            put("9648", "Mystery")
            put("10749", "Romance")
            put("878", "Science Fiction")
            put("10770", "TV Movie")
            put("53", "Thriller")
            put("10752", "War")
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