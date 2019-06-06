package com.qartf.popularmovies

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.qartf.popularmovies.database.MovieDatabase
import com.qartf.popularmovies.database.MovieDatabaseDao
import com.qartf.popularmovies.repository.MovieFactory
import com.qartf.popularmovies.utility.asDatabaseModel
import com.qartf.popularmovies.utility.getValue
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDatabaseDaoTest {
    private lateinit var movieDatabase: MovieDatabase
    private lateinit var movieDatabaseDao: MovieDatabaseDao
    private val movieFactory = MovieFactory()
    private val movieA = movieFactory.createMovie().asDatabaseModel()
    private val movieB = movieFactory.createMovie().asDatabaseModel()
    private val movieC = movieFactory.createMovie().asDatabaseModel()
    private val movieD = movieFactory.createMovie().asDatabaseModel()
    private val movieE = movieFactory.createMovie().asDatabaseModel()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application
        movieDatabase = Room.inMemoryDatabaseBuilder(application, MovieDatabase::class.java).build()
        movieDatabaseDao = movieDatabase.movieDatabaseDao()

        movieDatabaseDao.insert(movieA)
        movieDatabaseDao.insert(movieB)
        movieDatabaseDao.insert(movieC)
        movieDatabaseDao.insert(movieD)
        movieDatabaseDao.insert(movieE)
    }

    @After
    fun closeDb() {
        movieDatabase.close()
    }

    @Test
    fun testGetAllMovies() {
        val movieList = getValue(movieDatabaseDao.getAllMovies())
        assertThat(movieList.size, Matchers.equalTo(5))
    }

    @Test
    fun testGetMovieWithId() {
        assertThat(getValue(movieDatabaseDao.getMovieWithId(movieA.id))!!.id, Matchers.equalTo(movieA.id))
        assertThat(getValue(movieDatabaseDao.getMovieWithId(movieB.id))!!.id, Matchers.equalTo(movieB.id))
        assertThat(getValue(movieDatabaseDao.getMovieWithId(movieC.id))!!.id, Matchers.equalTo(movieC.id))
    }

    @Test
    fun testDeleteMovie() {
        movieDatabaseDao.deleteMovie(movieA.id)
        movieDatabaseDao.deleteMovie(movieB.id)
        movieDatabaseDao.deleteMovie(movieC.id)
        assertNull(getValue(movieDatabaseDao.getMovieWithId(movieA.id)))
        assertNull(getValue(movieDatabaseDao.getMovieWithId(movieB.id)))
        assertNull(getValue(movieDatabaseDao.getMovieWithId(movieC.id)))
    }

    @Test
    fun testClear() {
        movieDatabaseDao.clear()
        val movieList = getValue(movieDatabaseDao.getAllMovies())
        assertThat(movieList.size, Matchers.equalTo(0))
    }

}