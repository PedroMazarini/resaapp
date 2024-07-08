package com.mazarini.resa.ui.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

fun Context.isWifiConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    connectivityManager?.let {
        val network = it.activeNetwork ?: return false
        val capabilities = it.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }
    return false
}

fun Context.showMessage(
    message: String,
    duration: Int = Toast.LENGTH_LONG,
) {
    Toast.makeText(
        this,
        message,
        duration,
    ).show()
}