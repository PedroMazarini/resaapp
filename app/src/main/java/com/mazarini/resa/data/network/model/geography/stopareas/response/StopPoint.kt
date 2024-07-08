package com.mazarini.resa.data.network.model.geography.stopareas.response

import java.time.OffsetDateTime

/**
 * 
 *
 * @param gid GID for stop point.
 * @param stopAreaGid GID for stop area.
 * @param stopAreaNumber Number for stop area.
 * @param number Number for stop point.
 * @param name Name of stop point.
 * @param shortName Short name for stop point.
 * @param abbreviation Abbreviation code for stop point.
 * @param designation Designation of stop point.
 * @param localNumber Local number of stop point.
 * @param municipality 
 * @param tariffZones ?????
 * @param transportAuthority 
 * @param isForBoardning Boarding is/not allowed for stop point.
 * @param isForAlighting Alighting is/not allowed for stop point.
 * @param isRegularTraffic Regular traffic is/not at the stop point.
 * @param isFlexibleBusService Flexible bus service is/not at the stop point.
 * @param isFlexibleTaxiService Flexible taxi service is/not at the stop point.
 * @param isSpecialVehicleTransportConnectionPoint Special vehicle connection point is/not at the stop point.
 * @param geometry 
 * @param validFromDate Date when stop point is valid from.
 * @param validUpToDate Date when stop point is valid up to.
 */

data class StopPoint (
    val gid: String? = null,
    val stopAreaGid: String? = null,
    val stopAreaNumber: Int? = null,
    val number: Int? = null,
    val name: String? = null,
    val shortName: String? = null,
    val abbreviation: String? = null,
    val designation: String? = null,
    val localNumber: Int? = null,
    val municipality: Municipality? = null,
    val tariffZones: List<TariffZone>? = null,
    val transportAuthority: TransportAuthority? = null,
    val isForBoardning: Boolean? = null,
    val isForAlighting: Boolean? = null,
    val isRegularTraffic: Boolean? = null,
    val isFlexibleBusService: Boolean? = null,
    val isFlexibleTaxiService: Boolean? = null,
    val isSpecialVehicleTransportConnectionPoint: Boolean? = null,
    val geometry: Point? = null,
    val validFromDate: OffsetDateTime? = null,
    val validUpToDate: OffsetDateTime? = null
)
