package com.mazarini.resa.global.analytics

import android.util.Log
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import com.mazarini.resa.BuildConfig.DEBUG

var isUnitTest = false
const val TAG = "AppDebug"

fun logd(className: String?, message: String) {
    if (DEBUG && !isUnitTest) {
        Log.d(TAG, "$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}

fun loge(tag: String, params: Map<String, String> = mapOf()) {
    if (params.isEmpty()) {
        Log.e("Logger Error", tag)
    } else {
        Log.e(tag.take(22), params.toString())
    }
    val name = tag.ifEmpty { "Event" }
    Firebase.analytics.logEvent(name) {
        params.forEach { (key, value) ->
            param(key, value)
        }
    }
}
