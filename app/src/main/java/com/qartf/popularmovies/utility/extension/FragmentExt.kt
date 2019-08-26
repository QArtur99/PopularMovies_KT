package com.qartf.popularmovies.utility.extension

import androidx.fragment.app.Fragment
import com.qartf.popularmovies.ViewModelFactory
import com.qartf.popularmovies.model.Result
import com.qartf.popularmovies.utility.ServiceLocator

fun Fragment.getVmFactory(prefResult: Result? = null): ViewModelFactory {
    val repository = ServiceLocator.instance(requireContext().applicationContext).getRepository()
    return ViewModelFactory(repository, prefResult)
}