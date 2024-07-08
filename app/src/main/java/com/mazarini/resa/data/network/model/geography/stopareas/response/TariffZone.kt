package com.mazarini.resa.data.network.model.geography.stopareas.response

import java.time.OffsetDateTime

/**
 * 
 *
 * @param gid GID for tariff zone.
 * @param name Name of tariff zone.
 * @param shortName Short name for tariff zone.
 * @param number Number for tariff zone.
 * @param code Code for tariff zone.
 * @param geometry 
 * @param validFromDate Date when tariff zone is valid from.
 * @param validUpToDate Date when tariff zone is valid up to.
 */
data class TariffZone (
    val gid: String? = null,
    val name: String? = null,
    val shortName: String? = null,
    val number: Int? = null,
    val code: String? = null,
    val geometry: Polygon? = null,
    val validFromDate: OffsetDateTime? = null,
    val validUpToDate: OffsetDateTime? = null,
)
