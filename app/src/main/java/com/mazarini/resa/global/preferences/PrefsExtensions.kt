package com.mazarini.resa.global.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

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

suspend fun DataStore<Preferences>.collect(key: String) =
    this.data.map { it[stringPreferencesKey(key)].orEmpty() }

suspend fun <T : Enum<T>> DataStore<Preferences>.getEnumList(key: String, enumClass: Class<T>): List<T> {
    val storedValue = this.getString(key)
    return if (!storedValue.isNullOrEmpty()) {
        storedValue.split(",").mapNotNull {
            try {
                enumClass.enumConstants?.firstOrNull { enumValue -> enumValue.name == it }
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    } else {
        emptyList()
    }
}

fun <T : Enum<T>> DataStore<Preferences>.collectEnumList(key: String, enumClass: Class<T>): Flow<List<T>> {
    return this.data.map { preferences ->
        val storedValue = preferences[stringPreferencesKey(key)]
        if (!storedValue.isNullOrEmpty()) {
            storedValue.split(",").mapNotNull {
                try {
                    enumClass.enumConstants?.firstOrNull { enumValue -> enumValue.name == it }
                } catch (e: IllegalArgumentException) {
                    null
                }
            }
        } else {
            emptyList()
        }
    }
}

suspend fun <T : Enum<T>> DataStore<Preferences>.setEnumList(key: String, enumList: List<T>) {
    val valueToStore = enumList.joinToString(",") { it.name }
    this.putString(key, valueToStore)
}