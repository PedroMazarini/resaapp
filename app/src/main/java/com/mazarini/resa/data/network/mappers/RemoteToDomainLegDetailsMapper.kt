package com.mazarini.resa.data.network.mappers

import com.mazarini.resa.data.network.model.travelplanner.journeydetails.CallDetails
import com.mazarini.resa.data.network.model.travelplanner.journeydetails.TripLegDetails
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.ArrivalAccessLink
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.ConnectionLink
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.CoordinateInfo
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.DepartureAccessLink
import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.model.journey.Destination
import com.mazarini.resa.domain.model.journey.JourneyTimes
import com.mazarini.resa.domain.model.journey.LegStop
import com.mazarini.resa.domain.model.journey.Platform
import com.mazarini.resa.global.Mapper
import com.mazarini.resa.global.extensions.or
import com.mazarini.resa.global.extensions.orFalse
import com.mazarini.resa.global.extensions.parseRfc3339
import com.mazarini.resa.global.extensions.safeLet
import com.mazarini.resa.global.extensions.time_HH_mm
import com.mazarini.resa.global.analytics.loge
import java.util.Date
import com.mazarini.resa.data.network.model.travelplanner.journeydetails.JourneyDetailsResponse as RemoteLegDetails
import com.mazarini.resa.domain.model.journey.LegDetails as DomainLegDetails

class RemoteToDomainLegDetailsMapper : Mapper<RemoteLegDetails, List<DomainLegDetails>> {
    override fun map(value: RemoteLegDetails): List<DomainLegDetails> {
        val details = mutableListOf<DomainLegDetails>()

        value.departureAccessLink?.let {
            details.add(mapDepartLegDetails(it))
        }
        value.tripLegs?.let {
            it.forEachIndexed { index, remoteLegDetails ->
                val isLastLeg = index == it.lastIndex && value.arrivalAccessLink == null
                details.add(mapTransportLegDetails(remoteLegDetails, isLastLeg))
            }
        }
        value.connectionLinks?.let {
            it.forEach {
                details.add(mapConnectionLegDetails(it))
            }
        }
        value.arrivalAccessLink?.let {
            details.add(mapArrivalLegDetails(it))
        }
        return details
    }

    private fun mapDepartLegDetails(value: DepartureAccessLink): DomainLegDetails =
        DomainLegDetails(
            index = DEPART_INDEX,
            isLastLeg = false,
            pathWay = value.linkCoordinates?.getPathway().orEmpty(),
        )

    private fun mapConnectionLegDetails(value: ConnectionLink): DomainLegDetails =
        DomainLegDetails(
            index = value.journeyLegIndex or 0,
            isLastLeg = false,
            pathWay = value.linkCoordinates?.getPathway().orEmpty(),
        )

    private fun mapArrivalLegDetails(value: ArrivalAccessLink): DomainLegDetails =
        DomainLegDetails(
            index = ARRIVAL_INDEX,
            isLastLeg = true,
            destination = value.getArrivalDestination(),
            pathWay = value.linkCoordinates?.getPathway().orEmpty(),
        )

    private fun mapTransportLegDetails(value: TripLegDetails, isLastLeg: Boolean): DomainLegDetails {
        val destination = if (isLastLeg) {
            value.getDestination()
        } else null
        return DomainLegDetails(
            index = value.journeyLegIndex or 0,
            isLastLeg = isLastLeg,
            platForm = value.getPlatform(),
            destination = destination,
            legStops = value.getLegStops(),
            pathWay = value.serviceJourneys
                ?.first()
                ?.serviceJourneyCoordinates
                ?.getPathway()
                .orEmpty(),
        )
    }

    private fun ArrivalAccessLink.getArrivalDestination(): Destination {
        return Destination(
            name = destination?.name.orEmpty(),
            arrivalTime = getArrivalTime(),
        )
    }

    private fun TripLegDetails.getDestination(): Destination {
        val lastCall = callsOnTripLeg?.lastOrNull()
        return lastCall?.let {
            Destination(
                name = lastCall.stopPoint.name,
                platform = mapPlatform(lastCall.plannedPlatform, lastCall.estimatedPlatform),
                arrivalTime = lastCall.getArrivalTimes(),
            )
        } ?: error("$TAG Destination not found")
    }

    private fun CallDetails.getArrivalTimes(): JourneyTimes {
        return try {
            if (this.isArrivalAsPlanned()) {
                com.mazarini.resa.domain.model.journey.JourneyTimes.Planned(
                    time = plannedArrivalTime?.parseRfc3339() ?: Date(),
                    isLiveTracking = false,
                )
            } else {
                com.mazarini.resa.domain.model.journey.JourneyTimes.Changed(
                    planned = plannedArrivalTime?.parseRfc3339() ?: Date(),
                    estimated = estimatedArrivalTime?.parseRfc3339() ?: Date(),
                    isLiveTracking = true,
                )
            }
        } catch (e: Exception) {
            loge("$TAG failed to map Arrival Time: ${e.message}")
            error(e)
        }
    }

    private fun CallDetails.isArrivalAsPlanned(): Boolean =
        (estimatedArrivalTime == null) || (plannedArrivalTime == estimatedArrivalTime)

    private fun TripLegDetails.getLegStops(): List<LegStop> {
        return callsOnTripLeg?.mapNotNull {
            try {
                LegStop(
                    id = it.index.orEmpty(),
                    name = it.stopPoint.name,
                    time = it.getTime(),
                    coordinate = it.getCoordinates(),
                    isPartOfLeg = it.isOnTripLeg.orFalse,
                    isLegEnd = it.isTripLegStop.orFalse,
                    isLegStart = it.isTripLegStart.orFalse,
                )
            } catch (e: Exception) {
                loge("$TAG ${e.message}")
                null
            }
        }.orEmpty()
    }

    private fun CallDetails.getCoordinates(): Coordinate? {
        return safeLet(latitude, longitude) { lat, lon -> Coordinate(lat, lon) }
    }

    private fun CallDetails.getTime(): String {
        return estimatedOtherwisePlannedDepartureTime?.let {
            it.parseRfc3339()
            ?.time_HH_mm()
        } ?: run {
            estimatedOtherwisePlannedArrivalTime?.let {
                it.parseRfc3339()
                ?.time_HH_mm()
            }
        }.orEmpty()
    }

    private fun List<CoordinateInfo>.getPathway(): List<Coordinate> {
        return try {
            this.mapNotNull { coordinateInfo ->
                safeLet(coordinateInfo.latitude, coordinateInfo.longitude) { lat, lon ->
                    Coordinate(
                        lat = lat,
                        lon = lon,
                        isPartOfTrip = coordinateInfo.isOnTripLeg.orFalse,
                        isLegStart = coordinateInfo.isTripLegStart.orFalse,
                        isLegEnd = coordinateInfo.isTripLegStop.orFalse,
                    )
                }
            }
        } catch (e: Exception) {
            loge("$TAG ${e.message}")
            emptyList()
        }
    }

    private fun TripLegDetails.getPlatform(): Platform? {
        return callsOnTripLeg?.find { it.isTripLegStart.orFalse }?.let { origin ->
            mapPlatform(origin.plannedPlatform, origin.estimatedPlatform)
        }
    }

    private fun mapPlatform(planned: String?, estimated: String?): Platform? =
        if (planned != estimated && !estimated.isNullOrEmpty()) {
            Platform.Changed(
                name = estimated,
                oldName = planned.orEmpty(),
            )
        } else if (!planned.isNullOrEmpty()) {
            Platform.Planned(name = planned)
        } else null

    private fun TripLegDetails.getCoordinates(): Coordinate {
        return Coordinate(
            lat = tripLegCoordinates?.firstOrNull()?.latitude ?: 0.0,
            lon = tripLegCoordinates?.firstOrNull()?.longitude ?: 0.0
        )
    }

    companion object {
        private const val TAG = "RemoteToDomainLegDetailsMapper"
    }
}


