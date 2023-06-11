package com.resa.global.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

suspend fun DataStore<Preferences>.putInt(key: String, value: Int) {
    this.edit { settings ->
        settings[intPreferencesKey(key)] = value
    }
}

suspend fun DataStore<Preferences>.putLong(key: String, value: Long) {
    this.edit { settings ->
        settings[longPreferencesKey(key)] = value
    }
}

suspend fun DataStore<Preferences>.putString(key: String, value: String) {
    this.edit { settings ->
        settings[stringPreferencesKey(key)] = value
    }
}

suspend fun DataStore<Preferences>.getInt(key: String) =
    this.data.first()[intPreferencesKey(key)]

suspend fun DataStore<Preferences>.getLong(key: String) =
    this.data.first()[longPreferencesKey(key)]

suspend fun DataStore<Preferences>.getString(key: String) =
    this.data.first()[stringPreferencesKey(key)]