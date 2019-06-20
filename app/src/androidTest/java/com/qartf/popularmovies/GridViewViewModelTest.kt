package com.qartf.popularmovies

import android.app.Application
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.qartf.popularmovies.database.MovieDatabase
import com.qartf.popularmovies.database.MovieDatabaseDao
import com.qartf.popularmovies.gridView.GridViewViewModel
import com.qartf.popularmovies.network.TheMovieDbApi
import com.qartf.popularmovies.repository.FakeTheMovieDbApi
import com.qartf.popularmovies.repository.MovieFactory
import com.qartf.popularmovies.utility.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GridViewViewModelTest {

    private lateinit var movieDatabase: MovieDatabase
    private lateinit var gridViewViewModel: GridViewViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val movieFactory = MovieFactory()
    @Before
    fun setUp() {
        val application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application
        movieDatabase = Room.inMemoryDatabaseBuilder(application, MovieDatabase::class.java).build()

        val fakeApi = FakeTheMovieDbApi()
        fakeApi.sortByTest = Constants.SORT_BY_POPULARITY
        fakeApi.addPost(Constants.SORT_BY_POPULARITY, movieFactory.createMovie())
        fakeApi.addPost(Constants.SORT_BY_POPULARITY, movieFactory.createMovie())
        fakeApi.addPost(Constants.SORT_BY_POPULARITY, movieFactory.createMovie())

        // use a controlled service locator w/ fake API
        ServiceLocator.swap(
            object : DefaultServiceLocator(app = application) {
                override fun getMovieDb(): MovieDatabaseDao = movieDatabase.movieDatabaseDao()
                override fun getMovieDbApi(): TheMovieDbApi = fakeApi
            }
        )

        val repository = ServiceLocator.instance(application).getRepository()
        val prefResult = Result(2,  Constants.SORT_BY_POPULARITY,  Constants.SORT_BY_GENRE_DEFAULT)
        gridViewViewModel = GridViewViewModel(repository, prefResult)
        gridViewViewModel.onRecyclerItemClick(ResultMovie(View(application), movieFactory.createMovie()))
    }

    @After
    fun tearDown() {
        movieDatabase.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testDefaultValues() {
        assertEquals(getValue(gridViewViewModel.columns), 2)
        assertEquals(getValue(gridViewViewModel.sortBy).sortBy, Constants.SORT_BY_POPULARITY)
        assertNotNull(getValue(gridViewViewModel.listItem))
        assertTrue(getValue(gridViewViewModel.posts).size > 0)
    }
}