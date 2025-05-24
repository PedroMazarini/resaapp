package com.mazarini.resa.global.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.mazarini.resa.domain.model.ApiToken
import com.mazarini.resa.domain.model.TransportMode
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.global.extensions.ifEmpty
import com.mazarini.resa.global.extensions.orFalse
import com.mazarini.resa.global.model.ThemeSettings
import com.mazarini.resa.global.preferences.model.PreferredStop
import com.mazarini.resa.global.preferences.model.UserPreferences
import com.mazarini.resa.global.preferences.model.UserPreferencesSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsProvider
@Inject
constructor(
    @ApplicationContext private val context: Context,
) {
    suspend fun getCurrentJourneysQuery() = context.datastore.getString(CURRENT_JOURNEYS_QUERY)
    suspend fun setCurrentJourneysQuery(query: String) {
        context.datastore.putString(CURRENT_JOURNEYS_QUERY, query)
    }

    suspend fun getThemeSettings() = context.datastore.getString(THEME_SETTINGS)?.let {
        ThemeSettings.valueOf(it)
    } ?: ThemeSettings.SYSTEM

    suspend fun collectThemeSettings() = context.datastore.collect(THEME_SETTINGS).map {
        if (it.isEmpty()) ThemeSettings.SYSTEM else ThemeSettings.valueOf(it)
    }
    suspend fun setThemeSettings(settings: ThemeSettings) {
        context.datastore.putString(THEME_SETTINGS, settings.name)
    }

    suspend fun getPreferredTransportModes(): List<TransportMode> {
        val savedModes = context.datastore.getEnumList(PREFERRED_TRANSPORT_MODES, TransportMode::class.java)
        if (savedModes.isEmpty()) {
            setPreferredTransportModes(TransportMode.selectableModes())
            return TransportMode.selectableModes()
        }
        return savedModes
    }

    fun collectPreferredTransportModes(): Flow<List<TransportMode>> {
        val modes = context.datastore.collectEnumList(PREFERRED_TRANSPORT_MODES, TransportMode::class.java)
        return modes.map { it.ifEmpty { TransportMode.selectableModes() } }
    }

    suspend fun setPreferredTransportModes(preferred: List<TransportMode>) {
        context.datastore.setEnumList(PREFERRED_TRANSPORT_MODES, preferred)
    }

    suspend fun getToken(): ApiToken = context.customDataStore.data.first().token
    suspend fun setToken(token: ApiToken) {
        context.customDataStore.updateData { currentPrefs -> currentPrefs.copy(token = token) }
    }

    suspend fun getPreferredDeparture(): PreferredStop? {
        return context.customDataStore.data.first().preferredStop
    }

    suspend fun setPreferredDeparture(preferredStop: PreferredStop?) {
        context.customDataStore.updateData { currentPrefs ->
            currentPrefs.copy(preferredStop = preferredStop)
        }
    }

    suspend fun getSavedJourney(): Journey? {
        return context.customDataStore.data.first()?.savedJourney
    }

    suspend fun setSavedJourney(journey: Journey?) {
        context.customDataStore.updateData { currentPrefs ->
            currentPrefs?.copy(savedJourney = journey) ?: UserPreferences(savedJourney = journey)
        }
    }

    suspend fun hasSeenOnboarding(): Boolean {
        return context.customDataStore.data.first()?.hasSeenOnboarding.orFalse
    }

    suspend fun setSeenOnboarding() {
        context.customDataStore.updateData { currentPrefs ->
            currentPrefs.copy(hasSeenOnboarding = true)
        }
    }

    suspend fun getLanguage(): String? {
        return context.customDataStore.data.first().language
    }

    suspend fun getUserPrefs(): UserPreferences {
        return context.customDataStore.data.first()
    }

    suspend fun setLanguage(language: String) {
        context.customDataStore.updateData { currentPrefs ->
            currentPrefs?.copy(language = language) ?: UserPreferences(language = language)
        }
    }

    suspend fun setSeenSaveQueryFeat() {
        context.customDataStore.updateData { currentPrefs ->
            currentPrefs.copy(hasSeenSaveQueryFeat = true)
        }
    }

    suspend fun setSeenAddHomeFeat() {
        context.customDataStore.updateData { currentPrefs ->
            currentPrefs?.copy(hasSeenAddHomeFeat = true) ?: UserPreferences(hasSeenAddHomeFeat = true)
        }
    }

    fun userPreferencesFlow() = context.customDataStore.data

    companion object {
        const val SETTINGS_STORE = "settings_store"
        const val THEME_SETTINGS = "theme_settings"
        const val CURRENT_JOURNEYS_QUERY = "current_journeys_query"
        const val PREFERRED_TRANSPORT_MODES = "preferred_transport_modes"
        private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_STORE)
        private val Context.customDataStore: DataStore<UserPreferences> by dataStore("custom_user_prefs.json", serializer = UserPreferencesSerializer)
    }
}
