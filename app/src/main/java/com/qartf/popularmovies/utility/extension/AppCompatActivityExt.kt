package com.qartf.popularmovies.utility.extension

import androidx.appcompat.app.AppCompatActivity
import com.qartf.popularmovies.di.ViewModelFactory
import com.qartf.popularmovies.di.ServiceLocator

fun AppCompatActivity.getVmFactory(): ViewModelFactory {
    val repository = ServiceLocator.instance(this.applicationContext).getRepository()
    return ViewModelFactory(repository)
}