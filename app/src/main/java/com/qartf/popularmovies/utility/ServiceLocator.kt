package com.qartf.popularmovies.utility

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.qartf.popularmovies.database.MovieDatabase
import com.qartf.popularmovies.database.MovieDatabaseDao
import com.qartf.popularmovies.network.RetrofitModule
import com.qartf.popularmovies.network.TheMovieDbApi
import com.qartf.popularmovies.repository.Repository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Super simplified service locator implementation to allow us to replace default implementations
 * for testing.
 */
interface ServiceLocator {
    companion object {
        @Volatile
        private var instance: ServiceLocator? = null
        private val LOCK = Any()
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(
                        app = context.applicationContext as Application
                    )
                }
                return instance!!
            }
        }

        /**
         * Allows tests to replace the default implementations.
         */
        @VisibleForTesting
        fun swap(locator: ServiceLocator) {
            instance = locator
        }
    }

    fun getRepository(): Repository

    fun getNetworkExecutor(): Executor

    fun getDiskIOExecutor(): Executor

    fun getMovieDb(): MovieDatabaseDao

    fun getMovieDbApi(): TheMovieDbApi
}

/**
 * default implementation of ServiceLocator that uses production endpoints.
 */
open class DefaultServiceLocator(val app: Application) : ServiceLocator {
    // thread pool used for disk access
    @Suppress("PrivatePropertyName")
    private val DISK_IO = Executors.newSingleThreadExecutor()

    // thread pool used for network requests
    @Suppress("PrivatePropertyName")
    private val NETWORK_IO = Executors.newFixedThreadPool(5)

    private val db by lazy {
        MovieDatabase.getInstance(app).movieDatabaseDao()
    }
    private val api by lazy {
        RetrofitModule.devbytes
    }

    override fun getRepository(): Repository {
        return Repository(
            productDatabase = getMovieDb(),
            api = getMovieDbApi(),
            diskExecutor = getDiskIOExecutor(),
            networkExecutor = getNetworkExecutor()
        )
    }

    override fun getNetworkExecutor(): Executor = NETWORK_IO

    override fun getDiskIOExecutor(): Executor = DISK_IO

    override fun getMovieDb(): MovieDatabaseDao = db

    override fun getMovieDbApi(): TheMovieDbApi = api
}