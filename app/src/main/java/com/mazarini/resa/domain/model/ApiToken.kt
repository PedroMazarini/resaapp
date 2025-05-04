package com.mazarini.resa.domain.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@OptIn(InternalSerializationApi::class)
data class ApiToken(
    val expireMilli: Long,
    val token: String,
)
