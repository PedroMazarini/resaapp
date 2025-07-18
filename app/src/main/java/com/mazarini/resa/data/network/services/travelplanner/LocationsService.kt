package com.mazarini.resa.data.network.services.travelplanner

import com.mazarini.resa.data.network.model.travelplanner.location.LocationsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface LocationsService {

    @GET("/pr/v4/locations/{queryMode}")
    suspend fun queryLocation(
        @Header("Authorization") auth: String,
        @Path("queryMode") queryMode: String,
        @QueryMap queryMap: Map<String, String>,
        @Query("types") types: List<String>,
    ): LocationsResponse

    companion object {
        const val PAGE_SIZE = 10
    }
}
