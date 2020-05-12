package com.qartf.popularmovies

import android.content.Intent
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import com.qartf.popularmovies.data.database.MovieDatabase
import com.qartf.popularmovies.data.network.TheMovieDbApi
import com.qartf.popularmovies.di.createNetworkExecutor
import com.qartf.popularmovies.di.createRepository
import com.qartf.popularmovies.di.domainModule
import com.qartf.popularmovies.di.uiModule
import com.qartf.popularmovies.repository.FakeTheMovieDbApi
import com.qartf.popularmovies.repository.MovieFactory
import com.qartf.popularmovies.ui.MainActivity
import com.qartf.popularmovies.ui.MovieDetailActivity
import com.qartf.popularmovies.ui.gridView.GridViewPagingAdapter
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_POPULARITY
import junit.framework.TestCase.assertEquals
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.koin.test.KoinTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class MainActivityTest : KoinTest {

    @get:Rule
    var testRule = CountingTaskExecutorRule()

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    private val movieFactory = MovieFactory()
    private lateinit var movieDatabase: MovieDatabase
    private lateinit var application: TestApp

    @Before
    fun init() {
        val fakeApi = FakeTheMovieDbApi()
        fakeApi.sortByTest = SORT_BY_POPULARITY
        fakeApi.addPost(SORT_BY_POPULARITY, movieFactory.createMovie())
        fakeApi.addPost(SORT_BY_POPULARITY, movieFactory.createMovie())
        fakeApi.addPost(SORT_BY_POPULARITY, movieFactory.createMovie())

        application = getInstrumentation().targetContext.applicationContext as TestApp
        movieDatabase = Room.inMemoryDatabaseBuilder(application, MovieDatabase::class.java).build()
        application.injectModule(domainModule, uiModule, module {
            single { fakeApi as TheMovieDbApi }
            single { movieDatabase.movieDatabaseDao() }
            single { createRepository(get(), get(), get(), get()) }
            single { createNetworkExecutor() }
        })
        val intent = Intent(application, MainActivity::class.java)
        activityTestRule.launchActivity(intent)
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun useAppContext() {
        val appContext = getInstrumentation().targetContext.applicationContext
        assertEquals("com.qartf.popularmovies", appContext.packageName)
    }

    @Test
    fun showSomeResults() {
        val recyclerView = activityTestRule.activity.findViewById<RecyclerView>(R.id.recyclerView)
        assertThat(recyclerView.adapter, notNullValue())
        waitForAdapterChange(recyclerView)
        assertThat(recyclerView.adapter?.itemCount, `is`(3))
    }

    @Test
    fun validateIntentSentToPackage() {
        onView(withId(R.id.recyclerView)).perform(
            actionOnItemAtPosition<GridViewPagingAdapter.MovieViewHolder>(1, click())
        )
        intended(hasComponent(MovieDetailActivity::class.java.name))
    }

    private fun waitForAdapterChange(recyclerView: RecyclerView) {
        val latch = CountDownLatch(1)
        getInstrumentation().runOnMainSync {
            recyclerView.adapter?.registerAdapterDataObserver(
                object : RecyclerView.AdapterDataObserver() {
                    override fun onChanged() {
                        latch.countDown()
                    }

                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        latch.countDown()
                    }
                })
        }
        testRule.drainTasks(1, TimeUnit.SECONDS)
        if (recyclerView.adapter?.itemCount ?: 0 > 0) {
            return
        }
        assertThat(latch.await(100, TimeUnit.SECONDS), `is`(true))
    }
}