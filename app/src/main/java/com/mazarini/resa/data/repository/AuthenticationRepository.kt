package com.mazarini.resa.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.mazarini.resa.data.network.datasource.abstraction.AuthenticationDatasource
import com.mazarini.resa.global.extensions.hasExpired
import com.mazarini.resa.global.extensions.plusSec
import com.mazarini.resa.global.analytics.logd
import com.mazarini.resa.global.preferences.PrefsProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(DelicateCoroutinesApi::class)
@Singleton
class AuthenticationRepository
@Inject
constructor(
    private val authenticationDatasource: AuthenticationDatasource,
    private val prefsProvider: PrefsProvider,
    @ApplicationContext private val context: Context,
) {

    private val tag = this::class.java.simpleName

    suspend fun refreshToken() {
        val expireDate = prefsProvider.getTokenExpire() ?: 0L
        if (expireDate == 0L || Date(expireDate).hasExpired(toleranceInMin = 5))
            requestToken()
        else logd(tag, "Token is still valid : ${prefsProvider.getToken()}")
    }

    private suspend fun requestToken() {
        try {
            val tokenResult = authenticationDatasource.getToken(deviceId())
            prefsProvider.setToken(tokenResult.token)
            prefsProvider.setTokenExpire(Date().plusSec(tokenResult.expireInSec).time)
            logd(tag, "Token refreshed successfully : ${tokenResult.token}")
        } catch (e: Exception) {
            logd(tag, "Token refresh failed")
        }

    }

    @SuppressLint("HardwareIds")
    private fun deviceId() = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )

}