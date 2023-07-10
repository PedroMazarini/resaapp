package com.resa.data.network.mappers

import com.resa.domain.model.journey.Departure
import com.resa.domain.model.journey.JourneyTimes
import com.resa.global.Mapper
import com.resa.global.extensions.isNotNullOrEmpty
import com.resa.global.extensions.orFalse
import com.resa.global.extensions.parseRfc3339
import com.resa.global.loge
import java.util.Date
import com.resa.data.network.model.journeys.response.Journey as RemoteJourney
import com.resa.domain.model.journey.Journey as DomainJourney
import com.resa.domain.model.journey.Leg as DomainLeg

class RemoteToDomainJourneyMapper(
    private val legMapper: RemoteToDomainLegMapper,
    private val linkMapper: RemoteLinkToDomainLegMapper,
    private val departLinkMapper: RemoteDepartLinkToDomainLegMapper,
    private val arrivalLinkMapper: RemoteArrivalLinkToDomainLegMapper,
) : Mapper<RemoteJourney, DomainJourney> {
    override fun map(value: RemoteJourney): DomainJourney =
        DomainJourney(
            id = value.detailsReference.orEmpty(),
            departure = value.getDeparture(),
            durationInMinutes = value.getDuration(),
            hasAccessibility = value.getAccessibility(),
            hasAlert = value.getAlerts(),
            arrivalTimes = value.getArrivalTimes(),
            isDeparted = value.isDeparted.orFalse,
            legs = value.getLegs(),
        )

    private fun RemoteJourney.getDeparture(): Departure =
        Departure(
            departStopName = tripLegs?.first()?.origin?.stopPoint?.name.orEmpty(),
            departPlatform = tripLegs?.first()?.origin?.stopPoint?.platform.orEmpty(),
            time = getTime(),
        )

    private fun RemoteJourney.getArrivalTimes(): JourneyTimes {
        return tripLegs?.last()?.let { leg ->
            try {
                if (leg.plannedArrivalTime == leg.estimatedArrivalTime) {
                    JourneyTimes.Planned(
                        time = leg.plannedArrivalTime?.parseRfc3339() ?: Date(),
                        isLiveTracking = false,
                    )
                } else {
                    JourneyTimes.Changed(
                        planned = leg.plannedArrivalTime?.parseRfc3339() ?: Date(),
                        estimated = leg.estimatedArrivalTime?.parseRfc3339() ?: Date(),
                        isLiveTracking = true,
                    )
                }
            } catch (e: Exception) {
                loge("$TAG failed to map Arrival Time: ${e.message}")
                error(e)
            }
        } ?: error("Unable to map Arrival Times")
    }

    private fun RemoteJourney.getTime(): JourneyTimes {
        return tripLegs?.first()?.let { leg ->
            try {
                if (leg.plannedDepartureTime == leg.estimatedDepartureTime) {
                    JourneyTimes.Planned(
                        time = leg.plannedDepartureTime?.parseRfc3339() ?: Date(),
                        isLiveTracking = false,
                    )
                } else {
                    JourneyTimes.Changed(
                        planned = leg.plannedDepartureTime?.parseRfc3339() ?: Date(),
                        estimated = leg.estimatedDepartureTime?.parseRfc3339() ?: Date(),
                        isLiveTracking = true,
                    )
                }
            } catch (e: Exception) {
                loge("$TAG failed to map Time: ${e.message}")
                error(e)
            }
        } ?: error("Unable to map Departure Times")
    }

    private fun RemoteJourney.getDuration(): Int {
        var durationSum = 0
        tripLegs?.forEach {
            it.estimatedDurationInMinutes?.let { duration ->
                durationSum += duration
            } ?: run {
                it.plannedDurationInMinutes?.let { duration ->
                    durationSum += duration
                } ?: run {
                    error("Unable to map Duration")
                }
            }
        }
        return durationSum
    }

    private fun RemoteJourney.getAccessibility(): Boolean =
        tripLegs?.first()?.serviceJourney?.line?.isWheelchairAccessible.orFalse

    private fun RemoteJourney.getAlerts(): Boolean {
        var hasAlerts = false
        tripLegs?.forEach {
            if (it.notes.isNotNullOrEmpty() ||
                it.origin.notes.isNotNullOrEmpty() ||
                it.destination.notes.isNotNullOrEmpty()
            ) {
                hasAlerts = true
                return@forEach
            }
        }
        return hasAlerts
    }

    private fun RemoteJourney.getLegs(): List<DomainLeg> {
        val legs = mutableListOf<DomainLeg>()
        connectionLinks?.forEach {
            legs.add(linkMapper.map(it))
        }
        tripLegs?.forEach {
            legs.add(legMapper.map(it))
        }
        val sortedLegs = legs.sortedBy { it.index }.toMutableList()
        arrivalAccessLink?.let {
            sortedLegs.add(0, arrivalLinkMapper.map(it))
        }
        departureAccessLink?.let {
            sortedLegs.add(sortedLegs.size, departLinkMapper.map(it))
        }
        return sortedLegs
    }

    companion object {
        private const val TAG = "RemoteToDomainJourneyMapper"
    }
}
