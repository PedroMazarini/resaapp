package com.mazarini.resa.data.network.services.travelplanner

import com.mazarini.resa.data.network.model.travelplanner.stopareas.ArrivalsOrDeparturesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface StopDeparturesService {

    @GET("/pr/v4/stop-areas/{stopPointGid}/departures")
    suspend fun getStopDepartures(
        @Header("Authorization") auth: String,
        @Path("stopPointGid") stopAreaGid: String,
        @QueryMap queryMap: Map<String, String>,
    ): ArrivalsOrDeparturesResponse
}
