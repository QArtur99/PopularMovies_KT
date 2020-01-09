package com.qartf.popularmovies.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitModule {

    val BASE_URL = "http://api.themoviedb.org/3/movie/"

    fun provideRetrofit(baseURL: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    val devbytes: TheMovieDbApi = provideRetrofit(
        BASE_URL
    ).create(TheMovieDbApi::class.java)
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()