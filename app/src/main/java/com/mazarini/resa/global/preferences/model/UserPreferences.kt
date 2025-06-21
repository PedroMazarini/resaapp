package com.mazarini.resa.global.preferences.model

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.mazarini.resa.domain.model.ApiToken
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.global.preferences.Crypto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

@Serializable
@OptIn(InternalSerializationApi::class)
data class UserPreferences (
    val preferredStop: PreferredStop? = null,
    val hasSeenOnboarding: Boolean = false,
    val savedJourney: Journey? = null,
    val language: String? = null,
    val hasSeenSaveQueryFeat: Boolean = false,
    val hasSeenAddHomeFeat: Boolean = false,
    val token: ApiToken = ApiToken(0, ""),
)

object UserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue = UserPreferences()

    override suspend fun readFrom(input: InputStream): UserPreferences {
        return try {
            val encryptedBytes = withContext(Dispatchers.IO) {
                input.use { it.readBytes() }
            }

            if (encryptedBytes.isEmpty()) return defaultValue

            val base64Decoded = try {
                Base64.getDecoder().decode(encryptedBytes)
            } catch (e: IllegalArgumentException) {
                // Probably not Base64-encoded; assume it's a plain JSON file
                return Json.decodeFromString(encryptedBytes.decodeToString())
            }

            val decryptedBytes = Crypto.decrypt(base64Decoded)
            val decodedJsonString = decryptedBytes.decodeToString()
            Json.decodeFromString(decodedJsonString)

        } catch (e: SerializationException) {
            throw CorruptionException("Unable to read UserPrefs", e)
        } catch (e: Exception) {
            throw CorruptionException("Unexpected error reading UserPrefs", e)
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        val json = Json.encodeToString(t)
        val bytes = json.toByteArray()
        val encryptedBytes = Crypto.encrypt(bytes)
        val encryptedBytesBase64 = Base64.getEncoder().encode(encryptedBytes)
        withContext(Dispatchers.IO) {
            output.use {
                it.write(encryptedBytesBase64)
            }
        }
    }
}