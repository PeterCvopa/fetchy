package com.cvopa.peter.fetchy.util.context

import android.content.Context
import android.net.ConnectivityManager

@Suppress("DEPRECATION")
fun Context.hasInternet(): Boolean {
    val networkInfo =(this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}
