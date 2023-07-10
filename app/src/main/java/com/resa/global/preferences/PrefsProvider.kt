package com.resa.global.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsProvider
@Inject
constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_STORE)


    suspend fun getToken() = context.datastore.getString(VASTTRAFIK_TOKEN) ?: ""
    suspend fun setToken(token: String) {
        context.datastore.putString(VASTTRAFIK_TOKEN, token)
    }

    suspend fun getTokenExpire() = context.datastore.getLong(VASTTRAFIK_TOKEN_EXPIRE)
    suspend fun setTokenExpire(timestamp: Long) {
        context.datastore.putLong(VASTTRAFIK_TOKEN_EXPIRE, timestamp)
    }

    suspend fun getCurrentJourneysQuery() = context.datastore.getString(CURRENT_JOURNEYS_QUERY)
    suspend fun setCurrentJourneysQuery(query: String) {
        context.datastore.putString(CURRENT_JOURNEYS_QUERY, query)
    }

    companion object {
        const val SETTINGS_STORE = "settings_store"
        const val VASTTRAFIK_TOKEN = "vasttrafik_token"
        const val VASTTRAFIK_TOKEN_EXPIRE = "vasttrafik_token_expire"
        const val CURRENT_JOURNEYS_QUERY = "current_journeys_query"
    }
}
