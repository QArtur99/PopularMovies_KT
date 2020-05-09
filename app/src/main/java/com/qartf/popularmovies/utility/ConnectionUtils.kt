package com.qartf.popularmovies.utility

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

object ConnectionUtils {

    fun isConnectedToInternet(activity: Activity): Boolean {
        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            ?: return false

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkConnectionApiAboveApi23(cm)
        } else {
            checkConnectionApiBelow23(cm)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun checkConnectionApiAboveApi23(cm: ConnectivityManager): Boolean {
        val network = cm.activeNetwork
        cm.getNetworkCapabilities(network)?.also {
            when {
                it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                it.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> return true
                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE) -> return true
                it.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN) -> return true
            }
        }
        return false
    }

    @Suppress("DEPRECATION")
    private fun checkConnectionApiBelow23(cm: ConnectivityManager): Boolean {
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}