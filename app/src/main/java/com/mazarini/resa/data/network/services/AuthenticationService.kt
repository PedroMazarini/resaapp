package com.mazarini.resa.data.network.services

import com.mazarini.resa.BuildConfig
import com.mazarini.resa.data.network.model.TokenResponse
import retrofit2.http.*

interface AuthenticationService {

    @FormUrlEncoded
    @POST("/token")
    suspend fun requestToken(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("scope") clientId: String,
        @Header("Authorization") header: String = "Basic ${BuildConfig.VASTTRAFIK_API_KEY}",
    ): TokenResponse
}
