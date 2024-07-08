package com.mazarini.resa.data.network.mappers

import com.mazarini.resa.data.network.model.travelplanner.journeys.response.ArrivalAccessLink
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.DepartureAccessLink
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.DestinationLink
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.Line
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.Note
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.Severity
import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.model.TransportMode
import com.mazarini.resa.domain.model.journey.Departure
import com.mazarini.resa.domain.model.journey.Direction
import com.mazarini.resa.domain.model.journey.JourneyTimes
import com.mazarini.resa.domain.model.journey.LegColors
import com.mazarini.resa.domain.model.journey.LegType
import com.mazarini.resa.domain.model.journey.Warning
import com.mazarini.resa.domain.model.journey.WarningTypes
import com.mazarini.resa.domain.model.journey.orDefault
import com.mazarini.resa.global.Mapper
import com.mazarini.resa.global.extensions.asColor
import com.mazarini.resa.global.extensions.orDefaultColors
import com.mazarini.resa.global.extensions.orFalse
import com.mazarini.resa.global.extensions.orThrow
import com.mazarini.resa.global.extensions.parseRfc3339
import com.mazarini.resa.global.extensions.safeLet
import com.mazarini.resa.global.analytics.loge
import com.mazarini.resa.ui.util.MappingException
import java.util.Date
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.Journey as RemoteJourney
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.OccupancyLevel as DataOccupancyLevel
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.TripLeg as RemoteLeg
import com.mazarini.resa.domain.model.journey.Journey as DomainJourney
import com.mazarini.resa.domain.model.journey.Leg as DomainLeg
import com.mazarini.resa.domain.model.journey.OccupancyLevel as DomainOccupancyLevel

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
            transportDurationInMinutes = value.getTransportDuration(),
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
            directionName = tripLegs?.first()?.serviceJourney?.direction.orEmpty(),
            lineName = tripLegs?.first()?.serviceJourney?.line?.shortName.orEmpty(),
            colors = tripLegs?.first()?.serviceJourney?.line?.getColors().orDefault(),
            time = getTime(),
        )

    private fun Line.getColors(): LegColors = {
        LegColors(
            foreground = foregroundColor.asColor(),
            background = backgroundColor.asColor(),
            border = borderColor.asColor(),
        )
    }.orDefaultColors()

    private fun RemoteJourney.getArrivalTimes(): JourneyTimes {
        return tripLegs?.last()?.let { leg ->
            try {
                if (leg.isArrivalAsPlanned()) {
                    com.mazarini.resa.domain.model.journey.JourneyTimes.Planned(
                        time = leg.plannedArrivalTime?.parseRfc3339() ?: Date(),
                        isLiveTracking = false,
                    )
                } else {
                    com.mazarini.resa.domain.model.journey.JourneyTimes.Changed(
                        planned = leg.plannedArrivalTime?.parseRfc3339() ?: Date(),
                        estimated = leg.estimatedArrivalTime?.parseRfc3339() ?: Date(),
                        isLiveTracking = true,
                    )
                }
            } catch (e: Exception) {
                loge("${com.mazarini.resa.data.network.mappers.RemoteToDomainJourneyMapper.TAG} failed to map Arrival Time: ${e.message}")
                error(e)
            }
        } ?: run {
            com.mazarini.resa.domain.model.journey.JourneyTimes.Planned(
                time = destinationLink?.plannedArrivalTime?.parseRfc3339() ?: Date(),
                isLiveTracking = false,
            )
        }
    }

    private fun RemoteJourney.getTime(): JourneyTimes {
        return tripLegs?.first()?.let { leg ->
            try {
                if (leg.isDepartureAsPlanned()) {
                    com.mazarini.resa.domain.model.journey.JourneyTimes.Planned(
                        time = leg.plannedDepartureTime?.parseRfc3339() orThrow MappingException(leg),
                        isLiveTracking = false,
                    )
                } else {
                    com.mazarini.resa.domain.model.journey.JourneyTimes.Changed(
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
                loge("${com.mazarini.resa.data.network.mappers.RemoteToDomainJourneyMapper.TAG} failed to map Time: ${e.message}")
                error(e)
            }
        } ?: run {
            com.mazarini.resa.domain.model.journey.JourneyTimes.Planned(
                time = destinationLink?.plannedDepartureTime?.parseRfc3339() orThrow MappingException(
                    this
                ),
                isLiveTracking = false,
            )
        }
    }

    private fun RemoteJourney.getDuration(): Int {
        var durationSum = 0
        val depart = departureAccessLink?.let {
            it.estimatedDepartureTime?.parseRfc3339() ?: run {
                it.plannedDepartureTime?.parseRfc3339()
            }
        } ?: run {
            tripLegs?.first()?.estimatedOtherwisePlannedDepartureTime?.parseRfc3339()
        }
        val arrival = arrivalAccessLink?.let {
            it.estimatedArrivalTime?.parseRfc3339() ?: run {
                it.plannedArrivalTime?.parseRfc3339()
            }
        } ?: run {
            tripLegs?.last()?.estimatedOtherwisePlannedArrivalTime?.parseRfc3339()
        }
        if (depart != null && arrival != null) {
            durationSum = ((arrival.time - depart.time) / 60000).toInt()
        }
        return durationSum
    }

    private fun RemoteJourney.getTransportDuration(): Int {
        var durationSum = 0
        val depart = tripLegs?.first()?.estimatedOtherwisePlannedDepartureTime?.parseRfc3339()
        val arrival = tripLegs?.last()?.estimatedOtherwisePlannedArrivalTime?.parseRfc3339()

        if (depart != null && arrival != null) {
            durationSum = ((arrival.time - depart.time) / 60000).toInt()
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

        sortedLegs.addDepartureLink(departureAccessLink)
        sortedLegs.addArrivalLink(arrivalAccessLink)

        if (sortedLegs.isEmpty() && destinationLink != null) {
            sortedLegs.addDirectLink(destinationLink)
        }
        return sortedLegs
    }

    private fun DestinationLink.getWarnings(): List<Warning> {
        return notes?.map {
            Warning(
                message = it.text.orEmpty(),
                severity = it.severity?.let { severity ->
                    when (severity) {
                        Severity.normal -> com.mazarini.resa.domain.model.journey.WarningSeverity.MEDIUM
                        Severity.high -> com.mazarini.resa.domain.model.journey.WarningSeverity.HIGH
                        else -> com.mazarini.resa.domain.model.journey.WarningSeverity.LOW
                    }
                } ?: com.mazarini.resa.domain.model.journey.WarningSeverity.LOW
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

//    private fun MutableList<DomainLeg>.setArrivalTransportLegDetails(
//        arrivalLink: ArrivalAccessLink?,
//        arrivalLeg: RemoteLeg,
//    ) {
//        arrivalLink?.let { this }
//        ?: run {
//            last().let {
//                if (it.details is LegDetails.Transport) {
//                    set(
//                        lastIndex,
//                        it.copy(
//                            details = it.details.copy(
//                                arrivalLeg = ArrivalLeg.Details(
//                                    name = arrivalLeg.destination.stopPoint.name,
//                                    platform = arrivalLeg.destination.stopPoint.platform.orEmpty(),
//                                    arrivalTime = arrivalLeg.getTime(),
//                                )
//                            )
//                        )
//                    )
//                }
//            }
//        }
//    }

    private fun RemoteLeg.getTime(): JourneyTimes {
        return try {
            if (isArrivalAsPlanned()) {
                com.mazarini.resa.domain.model.journey.JourneyTimes.Planned(
                    time = plannedArrivalTime?.parseRfc3339() orThrow MappingException(this),
                    isLiveTracking = false,
                )
            } else {
                com.mazarini.resa.domain.model.journey.JourneyTimes.Changed(
                    planned = plannedArrivalTime?.parseRfc3339() orThrow MappingException(this),
                    estimated = estimatedArrivalTime?.parseRfc3339() orThrow MappingException(this),
                    isLiveTracking = true,
                )
            }
        } catch (e: Exception) {
            loge("${com.mazarini.resa.data.network.mappers.RemoteToDomainJourneyMapper.TAG} failed to map Time: ${e.message}")
            error(e)
        }
    }

    private fun RemoteLeg.isDepartureAsPlanned(): Boolean =
        plannedDepartureTime == estimatedOtherwisePlannedDepartureTime

//    private fun MutableList<DomainLeg>.checkForLastTransportLeg(
//        arrivalLink: ArrivalAccessLink?
//    ) {
//        arrivalLink?.let { this }
//            ?: run {
//            mapIndexed { index, leg ->
//                if (lastIndex == index) {
//                    (leg.details as? LegDetails.Transport)?.let {
//                        leg.copy(
//                            details = leg.details.copy(arrivalLeg = ArrivalLeg.None),
//                        )
//                    } ?: run { leg }
//                } else leg
//            }.toMutableList()
//        }
//    }

    private fun MutableList<DomainLeg>.addDirectLink(
        link: DestinationLink,
    ) {
       add(
           DomainLeg(
               index = 0,
               legType = LegType.DIRECT_LINK,
               transportMode = TransportMode.walk,
               durationInMinutes = link.estimatedDurationInMinutes ?: link.plannedDurationInMinutes ?: 0,
               departTime = link.getTime(),
               distanceInMeters = link.distanceInMeters ?: 0,
               name = link.origin?.name.orEmpty(),
               warnings = link.getWarnings(),
               direction = Direction(
                   from = link.getOriginCoordinates(),
                   to = link.getDestinationCoordinates(),
               ),
               directionName = link.destination?.name.orEmpty(),
               details = null,
            )
        )
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
