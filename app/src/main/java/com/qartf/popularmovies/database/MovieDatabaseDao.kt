package com.qartf.popularmovies.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MovieDatabaseDao {

    @Insert
    fun insert(product: MovieItem)

    @Update
    fun update(product: MovieItem)

    @Query("DELETE FROM movies")
    fun clear()

    @Query("SELECT * from movies WHERE id = :key")
    fun getMovieWithId(key: String): LiveData<MovieItem?>

    @Query("SELECT * from movies WHERE id = :key")
    fun getMovieWithId2(key: String): MovieItem?

    @Query("SELECT * FROM movies ORDER BY id DESC")
    fun getAllMovies(): LiveData<List<MovieItem>>

    @Query("DELETE FROM movies WHERE id = :key")
    fun deleteMovie(key: String)

    @Query("SELECT * FROM movies ORDER BY indexInResponse ASC")
    fun getAllMoviesDS(): DataSource.Factory<Int, MovieItem>

}

