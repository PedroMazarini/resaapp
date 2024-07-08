package com.mazarini.resa.global.preferences.model

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.mazarini.resa.domain.model.journey.Journey
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class UserPreferences (
    val preferredStop: PreferredStop? = null,
    val hasSeenOnboarding: Boolean = false,
    val savedJourney: Journey? = null,
    val language: String? = null,
    val hasSeenSaveQueryFeat: Boolean = false,
    val hasSeenAddHomeFeat: Boolean = false,
)

object UserPreferencesSerializer : Serializer<UserPreferences?> {
    override val defaultValue = null

    override suspend fun readFrom(input: InputStream): UserPreferences {
        try {
            return Json.decodeFromString(
                UserPreferences.serializer(), input.readBytes().decodeToString())
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read UserPrefs", serialization)
        }
    }

    override suspend fun writeTo(t: UserPreferences?, output: OutputStream) {
        t?.let {
            output.write(Json.encodeToString(UserPreferences.serializer(), t).encodeToByteArray())
        } ?: output.write(null)
    }
}