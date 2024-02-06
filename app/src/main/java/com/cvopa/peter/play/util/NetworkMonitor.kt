package com.cvopa.peter.play.util

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("DEPRECATION")
@Singleton
class NetworkMonitor @Inject constructor(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun isNetworkAvailable(): Boolean {
        //FIXME this just for demonstrative purpose
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}