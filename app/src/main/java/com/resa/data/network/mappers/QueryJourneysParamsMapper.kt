package com.resa.data.network.mappers

import com.resa.domain.model.queryjourneys.QueryJourneysParams
import com.resa.global.Mapper

class QueryJourneysParamsMapper : Mapper<QueryJourneysParams, Map<String, String>> {
    override fun map(value: QueryJourneysParams): Map<String, String> {
        val map = mutableMapOf<String, String>()

        value.originGid?.let { map["originGid"] = it }
        value.destinationGid?.let { map["destinationGid"] = it }
        value.originName?.let { map["originName"] = it }
        value.destinationName?.let { map["destinationName"] = it }
        value.originLatitude?.let { map["originLatitude"] = it.toString() }
        value.destinationLatitude?.let { map["destinationLatitude"] = it.toString() }
        value.originLongitude?.let { map["originLongitude"] = it.toString() }
        value.destinationLongitude?.let { map["destinationLongitude"] = it.toString() }
        value.dateTime?.let { map["dateTime"] = it }
        value.dateTimeRelatesTo?.let { map["dateTimeRelatesTo"] = it.name }
        value.paginationReference?.let { map["paginationReference"] = it }
        value.limit?.let { map["limit"] = it.toString() }
        value.onlyDirectConnections?.let { map["onlyDirectConnections"] = it.toString() }
        value.includeNearbyStopAreas?.let { map["includeNearbyStopAreas"] = it.toString() }
        value.viaGid?.let { map["viaGid"] = it }
        value.originWalk?.let { map["originWalk"] = it.enabled.toString() }
        value.destWalk?.let { map["destWalk"] = it.enabled.toString() }
        value.originBike?.let { map["originBike"] = it.enabled.toString() }
        value.destBike?.let { map["destBike"] = it.enabled.toString() }
        value.totalBike?.let { map["totalBike"] = it.enabled.toString() }
        value.originCar?.let { map["originCar"] = it.enabled.toString() }
        value.destCar?.let { map["destCar"] = it.enabled.toString() }
        value.originPark?.let { map["originPark"] = it.enabled.toString() }
        value.destPark?.let { map["destPark"] = it.enabled.toString() }
        value.interchangeDurationInMinutes?.let { map["interchangeDurationInMinutes"] = it.toString() }
        value.includeOccupancy?.let { map["includeOccupancy"] = it.toString() }

        return map
    }
}
