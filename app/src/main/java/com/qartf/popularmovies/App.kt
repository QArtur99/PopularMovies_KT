package com.qartf.popularmovies

import android.app.Application
import com.qartf.popularmovies.di.dataModule
import com.qartf.popularmovies.di.domainModule
import com.qartf.popularmovies.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    private val TAG = App::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        startKoin()
    }

    private fun startKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(dataModule, domainModule, uiModule))
        }
    }
}