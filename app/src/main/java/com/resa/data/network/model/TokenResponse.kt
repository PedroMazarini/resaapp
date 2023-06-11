package com.resa.data.network.model

import com.squareup.moshi.Json

data class TokenResponse(
    @Json(name = "scope") val scope: String,
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "expires_in") val expireInSec: String,
    @Json(name = "access_token") val token: String,
)
