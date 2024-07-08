package com.mazarini.resa.data.network.model.geography.stopareas.response

import java.time.OffsetDateTime

/**
 * 
 *
 * @param gid GID for stop area.
 * @param number Number for stop area.
 * @param name Name of stop area.
 * @param shortName Short name of stop area.
 * @param abbreviation Abbreviation for stop area.
 * @param municipality 
 * @param tariffZones 
 * @param interchangePriorityLevel Interchange priority level for stop area, indicates whether the stop is a high or low priority for interchange. There are four levels; 16 - not suitable, 12 - low priority, 8 - medium priority, 4 - high priority.
 * @param transportAuthority 
 * @param interchangePriorityMessage Interchange priority level in text format: Ej lämplig, Lågprioriterad, Prioriterad, Hög prioriterad.
 * @param defaultInterchangeDurationSeconds Default interchange duration in seconds for stop area.
 * @param geometry 
 * @param validFromDate Date from when stop area is valid from.
 * @param validUpToDate Date from when stop area is valid up to.
 * @param stopPoints 
 */
data class StopArea (
    val gid: String? = null,
    val number: Int? = null,
    val name: String? = null,
    val shortName: String? = null,
    val abbreviation: String? = null,
    val municipality: Municipality? = null,
    val tariffZones: List<TariffZone>? = null,
    val interchangePriorityLevel: Int? = null,
    val transportAuthority: TransportAuthority? = null,
    val interchangePriorityMessage: String? = null,
    val defaultInterchangeDurationSeconds: Int? = null,
    val geometry: Point? = null,
    val validFromDate: OffsetDateTime? = null,
    val validUpToDate: OffsetDateTime? = null,
    val stopPoints: List<StopPoint>? = null,
)
