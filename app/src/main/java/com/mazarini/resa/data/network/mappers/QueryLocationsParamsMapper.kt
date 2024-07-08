package com.mazarini.resa.data.network.mappers

import com.mazarini.resa.data.network.model.travelplanner.location.QueryLocationsParams
import com.mazarini.resa.data.network.model.travelplanner.location.QueryLocationsParams.ByCoordinates
import com.mazarini.resa.data.network.model.travelplanner.location.QueryLocationsParams.ByText
import com.mazarini.resa.global.Mapper

class QueryLocationsParamsMapper : Mapper<QueryLocationsParams, Map<String, String>> {
    override fun map(value: QueryLocationsParams): Map<String, String> {
        val map = mutableMapOf<String, String>()

        when (value) {
            is ByText -> {
                map["q"] = value.query
            }

            is ByCoordinates -> {
                map["latitude"] = value.latitude.toString()
                map["longitude"] = value.longitude.toString()
                value.radiusInMeters?.let { map["radiusInMeters"] = it.toString() }
            }
        }

        value.limit?.let { map["limit"] = it.toString() }
        value.offset?.let { map["offset"] = it.toString() }

        return map
    }
}
