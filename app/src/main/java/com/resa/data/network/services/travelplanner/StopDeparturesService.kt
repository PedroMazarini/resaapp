package com.resa.data.network.services.travelplanner

import com.resa.data.network.model.travelplanner.stopareas.ArrivalsOrDeparturesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface StopDeparturesService {

    @GET("/pr/v4/stop-points/{stopPointGid}/departures")
    suspend fun getStopDepartures(
        @Header("Authorization") auth: String,
        @Path("stopPointGid") stopPointGid: String,
    ): ArrivalsOrDeparturesResponse
}
