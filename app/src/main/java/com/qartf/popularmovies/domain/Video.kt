package com.qartf.popularmovies.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoContainer(
    val id: String,
    @Json(name = "results") val videos: List<Video>
)

@JsonClass(generateAdapter = true)
data class Video(
    val id: String,
    val iso_639_1: String,
    val iso_3166_1: String,
    val key: String,
    val name: String,
    val site: String,
    val size: String,
    val type: String
)