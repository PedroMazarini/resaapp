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
        try {
            val encryptedBytes = withContext(Dispatchers.IO) {
                input.use { it.readBytes() }
            }
            val encryptedBytesDecoded = Base64.getDecoder().decode(encryptedBytes)
            val decryptedBytes = Crypto.decrypt(encryptedBytesDecoded)
            val decodedJsonString = decryptedBytes.decodeToString()
            return Json.decodeFromString(decodedJsonString)
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read UserPrefs", serialization)
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