package com.qartf.popularmovies.domain.state

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED,
    DB_EMPTY
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status,
    val msg: String? = null
) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)
        val DATABASE = NetworkState(Status.DB_EMPTY)
        fun error(msg: String?) = NetworkState(Status.FAILED, msg)
    }
}