package com.artf.popularmovies.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.Executors

object RetrofitModule {

    val BASE_URL = "http://api.themoviedb.org/3/movie/"

    fun provideRetrofit(baseURL: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    val devbytes = provideRetrofit(BASE_URL).create(TheMovieDbAPI::class.java)
    val NETWORK_IO = Executors.newFixedThreadPool(5)
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()