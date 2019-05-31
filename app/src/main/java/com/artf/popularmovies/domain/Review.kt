package com.artf.popularmovies.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewContainer(
    val id: String,
    val page: String,
    @Json(name = "results") val reviews: List<Review>,
    val total_pages: String,
    val total_results: String
)

@JsonClass(generateAdapter = true)
data class Review(
    val author: String,
    val content: String,
    val id: String,
    val url: String
)