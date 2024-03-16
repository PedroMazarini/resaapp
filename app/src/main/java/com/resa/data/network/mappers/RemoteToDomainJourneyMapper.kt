package com.resa.data.network.mappers

import com.resa.data.network.model.travelplanner.journeys.response.ArrivalAccessLink
import com.resa.data.network.model.travelplanner.journeys.response.ConnectionLink
import com.resa.data.network.model.travelplanner.journeys.response.DepartureAccessLink
import com.resa.data.network.model.travelplanner.journeys.response.DestinationLink
import com.resa.data.network.model.travelplanner.journeys.response.Note
import com.resa.data.network.model.travelplanner.journeys.response.Severity
import com.resa.domain.model.Coordinate
import com.resa.domain.model.TransportMode
import com.resa.domain.model.journey.ArrivalLeg
import com.resa.domain.model.journey.Departure
import com.resa.domain.model.journey.JourneyTimes
import com.resa.domain.model.journey.Warning
import com.resa.domain.model.journey.WarningSeverity
import com.resa.domain.model.journey.WarningTypes
import com.resa.global.Mapper
import com.resa.global.extensions.orFalse
import com.resa.global.extensions.orThrow
import com.resa.global.extensions.parseRfc3339
import com.resa.global.extensions.safeLet
import com.resa.global.loge
import com.resa.ui.util.MappingException
import java.util.Date
import com.resa.data.network.model.travelplanner.journeys.response.Journey as RemoteJourney
import com.resa.data.network.model.travelplanner.journeys.response.OccupancyLevel as DataOccupancyLevel
import com.resa.data.network.model.travelplanner.journeys.response.TripLeg as RemoteLeg
import com.resa.domain.model.journey.Journey as DomainJourney
import com.resa.domain.model.journey.Leg as DomainLeg
import com.resa.domain.model.journey.OccupancyLevel as DomainOccupancyLevel

class RemoteToDomainJourneyMapper(
    private val legMapper: RemoteToDomainLegMapper,
    private val linkMapper: RemoteLinkToDomainLegMapper,
    private val departLinkMapper: RemoteDepartLinkToDomainLegMapper,
    private val arrivalLinkMapper: RemoteArrivalLinkToDomainLegMapper,
) : Mapper<RemoteJourney, DomainJourney> {
    override fun map(value: RemoteJourney): DomainJourney =
        DomainJourney(
            id = value.detailsReference.orEmpty(),
            detailsId = value.detailsReference.orEmpty(),
            departure = value.getDeparture(),
            durationInMinutes = value.getDuration(),
            hasAccessibility = value.getAccessibility(),
            warning = value.getWarnings(),
            arrivalTimes = value.getArrivalTimes(),
            isDeparted = value.isDeparted.orFalse,
            legs = value.getLegs(),
            occupancyLevel = value.getOccupancyLevel(),
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
                if (leg.isArrivalAsPlanned()) {
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
        } ?: run {
            JourneyTimes.Planned(
                time = destinationLink?.plannedArrivalTime?.parseRfc3339() ?: Date(),
                isLiveTracking = false,
            )
        }
    }

    private fun RemoteJourney.getTime(): JourneyTimes {
        return tripLegs?.first()?.let { leg ->
            try {
                if (leg.isDepartureAsPlanned()) {
                    JourneyTimes.Planned(
                        time = leg.plannedDepartureTime?.parseRfc3339() orThrow MappingException(leg),
                        isLiveTracking = false,
                    )
                } else {
                    JourneyTimes.Changed(
                        planned = leg.plannedDepartureTime?.parseRfc3339() orThrow MappingException(
                            leg
                        ),
                        estimated = leg.estimatedDepartureTime?.parseRfc3339() orThrow MappingException(
                            leg
                        ),
                        isLiveTracking = true,
                    )
                }
            } catch (e: Exception) {
                loge("$TAG failed to map Time: ${e.message}")
                error(e)
            }
        } ?: run {
            JourneyTimes.Planned(
                time = destinationLink?.plannedDepartureTime?.parseRfc3339() orThrow MappingException(
                    this
                ),
                isLiveTracking = false,
            )
        }
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
        } ?: run {
            durationSum = destinationLink?.plannedDurationInMinutes ?: 0
        }
        return durationSum
    }

    private fun RemoteJourney.getAccessibility(): Boolean =
        tripLegs?.first()?.serviceJourney?.line?.isWheelchairAccessible.orFalse

    private fun RemoteJourney.getWarnings(): WarningTypes {
        val warnings = mutableListOf<Note>()

        tripLegs?.forEach {
            it.notes?.let { note -> warnings.addAll(note) }
            it.origin.notes?.let { note -> warnings.addAll(note) }
            it.destination.notes?.let { note -> warnings.addAll(note) }
        }
        return if (warnings.isEmpty()) {
            return WarningTypes.NoWarning
        } else {
            var hasMediumWarning = false
            warnings.forEach {
                if (it.severity == Severity.high) return WarningTypes.HighWarning
                if (it.severity == Severity.normal) hasMediumWarning = true
            }
            if (hasMediumWarning) WarningTypes.MediumWarning else WarningTypes.LowWarning
        }
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
        tripLegs?.last()?.let { arrivalLeg ->
            sortedLegs.setArrivalTransportLegDetails(arrivalAccessLink, arrivalLeg)
        }
        sortedLegs.addDepartureLink(departureAccessLink)
        sortedLegs.addArrivalLink(arrivalAccessLink)

        if (sortedLegs.isEmpty()) {
            destinationLink?.let {
                sortedLegs.add(
                    DomainLeg.ArrivalLink(
                        index = 0,
                        transportMode = TransportMode.walk,
                        durationInMinutes = it.estimatedDurationInMinutes ?: 2,
                        departTime = it.getTime(),
                        distanceInMeters = it.distanceInMeters ?: 0,
                        name = it.origin?.name.orEmpty(),
                        destinationName = it.destination?.name.orEmpty(),
                        warnings = it.getWarnings(),
                        arriveTime = it.getArrivalTime(),
                        from = it.getOriginCoordinates(),
                        to = it.getDestinationCoordinates(),
                    )
                )
            }
        }
        return sortedLegs
    }

    private fun DestinationLink.getWarnings(): List<Warning> {
        return notes?.map {
            Warning(
                message = it.text.orEmpty(),
                severity = it.severity?.let { severity ->
                    when (severity) {
                        Severity.normal -> WarningSeverity.MEDIUM
                        Severity.high -> WarningSeverity.HIGH
                        else -> WarningSeverity.LOW
                    }
                } ?: WarningSeverity.LOW
            )
        }.orEmpty()
    }

    private fun DestinationLink.isArrivalAsPlanned(): Boolean =
        (estimatedArrivalTime == null) || (plannedArrivalTime == estimatedArrivalTime)

    private fun DestinationLink.getArrivalTime(): JourneyTimes {
        return try {
            if (isArrivalAsPlanned()) {
                JourneyTimes.Planned(
                    time = plannedArrivalTime?.parseRfc3339() orThrow MappingException(this),
                    isLiveTracking = false,
                )
            } else {
                JourneyTimes.Changed(
                    planned = plannedArrivalTime?.parseRfc3339() orThrow MappingException(this),
                    estimated = estimatedArrivalTime?.parseRfc3339() orThrow MappingException(this),
                    isLiveTracking = true,
                )
            }
        } catch (e: Exception) {
            loge("$TAG failed to map Time: ${e.message}")
            error(e)
        }
    }

    private fun DestinationLink.isDepartureAsPlanned(): Boolean =
        (estimatedDepartureTime == null) || (plannedDepartureTime == estimatedDepartureTime)

    private fun DestinationLink.getTime(): JourneyTimes {
        return try {
            if (isDepartureAsPlanned()) {
                JourneyTimes.Planned(
                    time = plannedDepartureTime?.parseRfc3339() orThrow MappingException(this),
                    isLiveTracking = false,
                )
            } else {
                JourneyTimes.Changed(
                    planned = plannedDepartureTime?.parseRfc3339() orThrow MappingException(this),
                    estimated = estimatedDepartureTime?.parseRfc3339() orThrow MappingException(this),
                    isLiveTracking = true,
                )
            }
        } catch (e: Exception) {
            loge("$TAG failed to map Time: ${e.message}")
            error(e)
        }
    }

    private fun MutableList<DomainLeg>.setArrivalTransportLegDetails(
        arrivalLink: ArrivalAccessLink?,
        arrivalLeg: RemoteLeg,
    ) {
        arrivalLink?.let { this }
        ?: run {
            last().let {
                if (it is DomainLeg.Transport) {
                    set(
                        lastIndex,
                        it.copy(
                            arrivalLeg = ArrivalLeg.Details(
                                name = arrivalLeg.destination.stopPoint.name,
                                arrivalTime = arrivalLeg.getTime(),
                            )
                        )
                    )
                }
            }
        }
    }

    private fun RemoteLeg.getTime(): JourneyTimes {
        return try {
            if (isDepartureAsPlanned()) {
                JourneyTimes.Planned(
                    time = plannedDepartureTime?.parseRfc3339() orThrow MappingException(this),
                    isLiveTracking = false,
                )
            } else {
                JourneyTimes.Changed(
                    planned = plannedDepartureTime?.parseRfc3339() orThrow MappingException(this),
                    estimated = estimatedDepartureTime?.parseRfc3339() orThrow MappingException(this),
                    isLiveTracking = true,
                )
            }
        } catch (e: Exception) {
            loge("$TAG failed to map Time: ${e.message}")
            error(e)
        }
    }

    private fun RemoteLeg.isDepartureAsPlanned(): Boolean =
        plannedDepartureTime == estimatedOtherwisePlannedDepartureTime

    private fun MutableList<DomainLeg>.checkForLastTransportLeg(
        arrivalLink: ArrivalAccessLink?
    ) {
        arrivalLink?.let { this }
            ?: run {
            mapIndexed { index, leg ->
                if (lastIndex == index) {
                    (leg as? DomainLeg.Transport)?.let {
                        leg.copy(
                            arrivalLeg = ArrivalLeg.None,
                        )
                    } ?: run { leg }
                } else leg
            }.toMutableList()
        }
    }

    private fun MutableList<DomainLeg>.addArrivalLink(
        arrivalAccessLink: ArrivalAccessLink?
    ) {
        arrivalAccessLink?.let { add(arrivalLinkMapper.map(it)) }
    }

    private fun MutableList<DomainLeg>.addDepartureLink(
        departureAccessLink: DepartureAccessLink?
    ) {
        departureAccessLink?.let { add(0, departLinkMapper.map(it)) }
    }

    private fun DestinationLink.getOriginCoordinates(): Coordinate? {
        return safeLet(origin?.latitude, origin?.longitude) { lat, lon ->
            Coordinate(lat = lat, lon = lon)
        }
    }

    private fun DestinationLink.getDestinationCoordinates(): Coordinate? {
        return safeLet(destination?.latitude, destination?.longitude) { lat, lon ->
            Coordinate(lat = lat, lon = lon)
        }
    }

    private fun RemoteLeg.isArrivalAsPlanned(): Boolean =
        plannedArrivalTime == estimatedOtherwisePlannedArrivalTime

    private fun RemoteJourney.getOccupancyLevel(): DomainOccupancyLevel {
        return occupancy?.let {
            when (it.level) {
                DataOccupancyLevel.low -> DomainOccupancyLevel.LOW
                DataOccupancyLevel.medium -> DomainOccupancyLevel.MEDIUM
                DataOccupancyLevel.high -> DomainOccupancyLevel.HIGH
                else -> DomainOccupancyLevel.UNKNOWN
            }
        } ?: DomainOccupancyLevel.UNKNOWN
    }

    companion object {
        private const val TAG = "RemoteToDomainJourneyMapper"
    }
}
