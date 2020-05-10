package com.qartf.popularmovies

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.qartf.popularmovies.data.database.MovieDatabase
import com.qartf.popularmovies.data.model.Result
import com.qartf.popularmovies.data.model.ResultMovie
import com.qartf.popularmovies.data.network.TheMovieDbApi
import com.qartf.popularmovies.di.createNetworkExecutor
import com.qartf.popularmovies.di.createRepository
import com.qartf.popularmovies.data.repository.Repository
import com.qartf.popularmovies.repository.FakeTheMovieDbApi
import com.qartf.popularmovies.repository.MovieFactory
import com.qartf.popularmovies.ui.gridView.GridViewViewModel
import com.qartf.popularmovies.utility.Constants
import com.qartf.popularmovies.utility.getValue
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

class GridViewViewModelTest : KoinTest {

    private val repository: Repository by inject()
    private lateinit var movieDatabase: MovieDatabase
    private lateinit var gridViewViewModel: GridViewViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val movieFactory = MovieFactory()

    @Before
    fun setUp() {
        val application =
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApp
        movieDatabase = Room.inMemoryDatabaseBuilder(application, MovieDatabase::class.java).build()

        val fakeApi = FakeTheMovieDbApi()
        fakeApi.sortByTest = Constants.SORT_BY_POPULARITY
        fakeApi.addPost(Constants.SORT_BY_POPULARITY, movieFactory.createMovie())
        fakeApi.addPost(Constants.SORT_BY_POPULARITY, movieFactory.createMovie())
        fakeApi.addPost(Constants.SORT_BY_POPULARITY, movieFactory.createMovie())
        application.injectModule(module {
            single { movieDatabase.movieDatabaseDao() }
            single { fakeApi as TheMovieDbApi }
            single { createRepository(get(), get(), get(), get()) }
            single { createNetworkExecutor() }
        })

        val prefResult = Result(
            2,
            Constants.SORT_BY_POPULARITY,
            Constants.SORT_BY_GENRE_DEFAULT
        )
        gridViewViewModel = GridViewViewModel(repository, prefResult)
        gridViewViewModel.onRecyclerItemClick(
            ResultMovie(View(application), movieFactory.createMovie())
        )
    }

    @After
    fun tearDown() {
        movieDatabase.close()
    }

    @Test
    fun testDefaultValues() {
        assertEquals(getValue(gridViewViewModel.columns), 2)
        assertEquals(getValue(gridViewViewModel.discoverMovie).sortBy, Constants.SORT_BY_POPULARITY)
        assertNotNull(getValue(gridViewViewModel.listItem))
        assertTrue(getValue(gridViewViewModel.posts).size > 0)
        Thread.sleep(2000)
    }
}