package com.qartf.popularmovies.utility.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.qartf.popularmovies.ViewModelFactory
import com.qartf.popularmovies.utility.ServiceLocator

fun AppCompatActivity.getVmFactory(): ViewModelFactory {
    val repository = ServiceLocator.instance(this.applicationContext).getRepository()
    return ViewModelFactory(repository)
}

inline fun <reified T : ViewModel> AppCompatActivity.getVm(): T {
    return ViewModelProviders.of(this, getVmFactory()).get(T::class.java)
}