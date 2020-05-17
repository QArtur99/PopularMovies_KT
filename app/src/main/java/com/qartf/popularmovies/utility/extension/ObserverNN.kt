package com.qartf.popularmovies.utility.extension

import androidx.lifecycle.Observer

class ObserverNN<T>(private val callback: (T) -> Unit) : Observer<T> {
    override fun onChanged(t: T) {
        t?.let { callback(t) }
    }
}