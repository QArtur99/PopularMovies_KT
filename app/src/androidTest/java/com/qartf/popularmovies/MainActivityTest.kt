package com.qartf.popularmovies

import android.app.Application
import android.content.Intent
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.qartf.popularmovies.gridView.GridViewPagingAdapter
import com.qartf.popularmovies.network.TheMovieDbApi
import com.qartf.popularmovies.repository.FakeTheMovieDbApi
import com.qartf.popularmovies.repository.MovieFactory
import com.qartf.popularmovies.utility.Constants.Companion.SORT_BY_POPULARITY
import com.qartf.popularmovies.utility.DefaultServiceLocator
import com.qartf.popularmovies.utility.ServiceLocator
import junit.framework.TestCase.assertEquals

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var testRule = CountingTaskExecutorRule()

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Rule
    @JvmField
    var activityDetailTestRule = IntentsTestRule(MainActivity::class.java)

    private val movieFactory = MovieFactory()
    @Before
    fun init() {
        val fakeApi = FakeTheMovieDbApi()
        fakeApi.sortByTest = SORT_BY_POPULARITY
        fakeApi.addPost(SORT_BY_POPULARITY, movieFactory.createMovie())
        fakeApi.addPost(SORT_BY_POPULARITY, movieFactory.createMovie())
        fakeApi.addPost(SORT_BY_POPULARITY, movieFactory.createMovie())

        val application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application
        // use a controlled service locator w/ fake API
        ServiceLocator.swap(
            object : DefaultServiceLocator(app = application) {
                override fun getMovieDbApi(): TheMovieDbApi = fakeApi
            }
        )

        val intent = Intent(application, MainActivity::class.java)
        activityTestRule.launchActivity(intent)
    }

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        assertEquals("com.qartf.popularmovies", appContext.packageName)
    }

    @Test
    @Throws(InterruptedException::class, TimeoutException::class)
    fun showSomeResults() {
        val recyclerView = activityTestRule.activity.findViewById<RecyclerView>(R.id.recyclerView)
        assertThat(recyclerView.adapter, notNullValue())
        waitForAdapterChange(recyclerView)
        assertThat(recyclerView.adapter?.itemCount, `is`(3))
    }

    @Test
    fun validateIntentSentToPackage() {
        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<GridViewPagingAdapter.MovieViewHolder>(1, click())
        )
        intended(hasComponent(MovieDetailActivity::class.java.name))
    }

    private fun waitForAdapterChange(recyclerView: RecyclerView) {
        val latch = CountDownLatch(1)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
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