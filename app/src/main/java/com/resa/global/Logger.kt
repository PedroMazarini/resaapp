package com.resa.global

import android.util.Log
import com.resa.BuildConfig.DEBUG

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

fun loge(message: String) {
    Log.e(TAG, message)
}

fun crashLog(msg: String?) {
    msg?.let {
        if (!DEBUG) {
            // TODO add crashlytics
//            FirebaseCrashlytics.getInstance().log(it)
        }
    }
}