package com.resa.data.network.mappers

import com.resa.data.network.model.travelplanner.journeydetails.CallDetails
import com.resa.domain.model.Coordinate
import com.resa.domain.model.journey.LegStop
import com.resa.domain.model.journey.Platform
import com.resa.global.Mapper
import com.resa.global.extensions.orFalse
import com.resa.global.extensions.orThrow
import com.resa.global.extensions.parseRfc3339
import com.resa.global.extensions.safeLet
import com.resa.global.extensions.time_HH_mm
import com.resa.global.loge
import com.resa.ui.util.MappingException
import com.resa.data.network.model.travelplanner.journeydetails.TripLegDetails as RemoteLegDetails
import com.resa.domain.model.journey.LegDetails as DomainLegDetails


class RemoteToDomainLegDetailsMapper : Mapper<RemoteLegDetails, DomainLegDetails> {
    override fun map(value: RemoteLegDetails): DomainLegDetails =
        DomainLegDetails.Details(
            index = value.journeyLegIndex ?: 0,
            platForm = value.getPlatform(),
            pathWay = value.getPathway(),
            legStops = value.getLegStops(),
        )

    private fun RemoteLegDetails.getLegStops(): List<LegStop> {
        return callsOnTripLeg?.mapNotNull {
            try {
                LegStop(
                    id = it.index.orEmpty(),
                    name = it.stopPoint.name,
                    time = it.getTime(),
                )
            } catch (e: Exception) {
                loge("$TAG ${e.message}")
                null
            }
        }.orEmpty()
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
    private fun RemoteLegDetails.getPathway(): List<Coordinate> {
        return tripLegCoordinates?.mapNotNull {
            safeLet(it.latitude, it.longitude) { lat, lon -> Coordinate(lat, lon) }
        }.orEmpty()
    }

    private fun RemoteLegDetails.getPlatform(): Platform? {
        return callsOnTripLeg?.find { it.isTripLegStart.orFalse }?.let { origin ->
            val (planned, estimated) = listOf(origin.plannedPlatform, origin.estimatedPlatform)
            if (planned != estimated && !estimated.isNullOrEmpty()) {
                Platform.Changed(name = estimated)
            } else if (!planned.isNullOrEmpty()) {
                Platform.Planned(name = planned)
            } else null
        }
    }
    private fun RemoteLegDetails.getCoordinates(): Coordinate {
        return Coordinate(
            lat = tripLegCoordinates?.firstOrNull()?.latitude ?: 0.0,
            lon = tripLegCoordinates?.firstOrNull()?.longitude ?: 0.0
        )
    }

    companion object {
        private const val TAG = "RemoteToDomainLegDetailsMapper"
    }
}
