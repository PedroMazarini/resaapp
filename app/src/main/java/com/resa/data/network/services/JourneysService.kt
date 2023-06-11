package com.resa.data.network.services

import com.resa.data.network.model.journeys.GetJourneysResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface JourneysService {

    @GET("/pr/v4/journeys")
    suspend fun queryJourneys(
        @Header("Authorization") auth: String,
        @QueryMap queryMap: Map<String, String>,
        @Query("transportModes") transportModes: List<String>,
        @Query("transportSubModes") transportSubModes: List<String>,
    ): GetJourneysResponse
}
