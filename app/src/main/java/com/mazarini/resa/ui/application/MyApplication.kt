package com.mazarini.resa.ui.application

import android.app.Application
//import com.facebook.flipper.android.AndroidFlipperClient
//import com.facebook.flipper.android.utils.FlipperUtils
//import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
//import com.facebook.flipper.plugins.inspector.DescriptorMapping
//import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
//import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
//import com.facebook.soloader.SoLoader
//import com.mazarini.resa.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//        SoLoader.init(this, false)
//
//        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
//            val client = AndroidFlipperClient.getInstance(this)
//            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
//            client.addPlugin(SharedPreferencesFlipperPlugin(this, "my_shared_preference_file"))
//            client.addPlugin(DatabasesFlipperPlugin(this))
//            client.start()
//        }
    }
}