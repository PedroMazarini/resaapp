package com.resa.data.network.services.travelplanner

import com.resa.data.network.model.travelplanner.journeys.GetJourneysResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface JourneysService {

    @GET("journeys")
    suspend fun queryJourneys(
        @Header("Authorization") auth: String,
        @QueryMap queryMap: Map<String, String>,
        @Query("transportModes") transportModes: List<String>,
        @Query("transportSubModes") transportSubModes: List<String>,
    ): GetJourneysResponse

    @GET
    suspend fun queryJourneysByUrl(
        @Header("Authorization") auth: String,
        @Url params: String,
    ): GetJourneysResponse

    companion object {
        const val PAGE_SIZE = 10
    }
}
