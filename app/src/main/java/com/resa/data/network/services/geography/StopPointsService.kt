package com.resa.data.network.services.geography

import com.resa.data.network.model.geography.stoppoints.StopPointsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface StopPointsService {

    /**
     * @param srid Spacial Reference Identifier. Default is 4326 (WGS84) LatLon format.
     */

    @GET("geo/v2/StopPoints")
    suspend fun getStopsAround(
        @Header("Authorization") auth: String,
        @QueryMap paramsMap: Map<String, String>,
        @Query("srid") srid: Int = 4326,
    ): StopPointsResponse
}
