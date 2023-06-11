package com.resa.data.network.model.journeys

import com.resa.data.network.model.journeys.response.TransportMode
import com.resa.data.network.model.journeys.response.TransportSubMode
import com.resa.data.network.model.location.QueryLocationsParams

data class QueryJourneysParams(
    val originGid: String? = null,
    val originName: String? = null,
    val originLatitude: Double? = null,
    val originLongitude: Double? = null,
    val destinationGid: String? = null,
    val destinationName: String? = null,
    val destinationLatitude: Double? = null,
    val destinationLongitude: Double? = null,
    val dateTime: String? = null,
    val dateTimeRelatesTo: QueryJourneysRelatesTo? = null,
    val paginationReference: String? = null,
    val limit: Int? = null,
    val transportModes: List<TransportMode>? = null,
    val transportSubModes: List<TransportSubMode>? = null,
    val onlyDirectConnections: Boolean? = null,
    val includeNearbyStopAreas: Boolean? = null,
    val viaGid: String? = null,
    val originWalk: OwnTransport? = null,
    val destWalk: OwnTransport? = null,
    val originBike: OwnTransport? = null,
    val destBike: OwnTransport? = null,
    val totalBike: OwnTransport? = null,
    val originCar: OwnTransport? = null,
    val destCar: OwnTransport? = null,
    val originPark: OwnTransport? = null,
    val destPark: OwnTransport? = null,
    val interchangeDurationInMinutes: Int? = null,
    val includeOccupancy: Boolean? = null,
)

fun QueryJourneysParams.transportModesNames(): List<String> {
    return transportModes?.map { it.name } ?: emptyList()
}

fun QueryJourneysParams.transportSubModesNames(): List<String> {
    return transportSubModes?.map { it.name } ?: emptyList()
}

enum class QueryJourneysRelatesTo {
    DEPARTURE,
    ARRIVAL,
}

data class OwnTransport(
    val enabled: Boolean,
    val minDistance: Int? = null,
    val maxDistance: Int? = null,
)