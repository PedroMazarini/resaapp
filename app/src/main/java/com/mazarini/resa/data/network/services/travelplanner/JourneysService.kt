package com.mazarini.resa.data.network.services.travelplanner

import com.mazarini.resa.data.network.model.travelplanner.journeydetails.JourneyDetailsResponse
import com.mazarini.resa.data.network.model.travelplanner.journeys.GetJourneysResponse
import com.mazarini.resa.data.network.model.travelplanner.position.Position
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.Url
import java.util.Locale

interface JourneysService {

    @GET("journeys")
    suspend fun queryJourneys(
        @Header("Authorization") auth: String,
        @Header("Accept-Language") language: String = Locale.getDefault().language,
        @QueryMap queryMap: Map<String, String>,
        @Query("transportModes") transportModes: List<String>,
        @Query("transportSubModes") transportSubModes: List<String>,
    ): GetJourneysResponse

    @GET
    suspend fun queryJourneysByUrl(
        @Header("Authorization") auth: String,
        @Url params: String,
    ): GetJourneysResponse

    @GET("/pr/v4/journeys/{detailsRef}/details")
    suspend fun getJourneyDetails(
        @Header("Authorization") auth: String,
        @Header("Accept-Language") language: String = Locale.getDefault().language,
        @Path("detailsRef") detailsRef: String,
        @Query("includes") includes: List<String>,
    ): JourneyDetailsResponse

    @GET("/pr/v4/positions")
    suspend fun getJourneyVehicles(
        @Header("Authorization") auth: String,
        @QueryMap queryMap: Map<String, String>,
    ): List<Position>

    companion object {
        const val PAGE_SIZE = 10
    }
}
