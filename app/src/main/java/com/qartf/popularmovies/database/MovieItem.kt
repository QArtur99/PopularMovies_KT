package com.qartf.popularmovies.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieItem(

    @PrimaryKey(autoGenerate = true)
    var itemId: Long = 0L,

    @ColumnInfo(name = "vote_count")
    var vote_count: String = "",

    @ColumnInfo(name = "id")
    var id: String = "",

    @ColumnInfo(name = "video")
    var video: Boolean = false,

    @ColumnInfo(name = "vote_average")
    var vote_average: String = "",

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "popularity")
    var popularity: String = "",

    @ColumnInfo(name = "poster_path")
    var poster_path: String = "",

    @ColumnInfo(name = "original_language")
    var original_language: String = "",

    @ColumnInfo(name = "original_title")
    var original_title: String = "",

    @ColumnInfo(name = "genre_ids")
    var genre_ids: String = "",

    @ColumnInfo(name = "backdrop_path")
    var backdrop_path: String = "",

    @ColumnInfo(name = "adult")
    var adult: Boolean = false,

    @ColumnInfo(name = "overview")
    var overview: String = "",

    @ColumnInfo(name = "release_date")
    var release_date: String = ""
) {
    var indexInResponse: Int = -1
}