package com.mazarini.resa.data.network.model.travelplanner.journeys.response

/**
 * @param gid The 16-digit Västtrafik gid of the tariff zone.
 * @param name The name of the tariff zone.
 * @param number The tariff zone number.
 * @param shortName The short name of the tariff zone.
 */
data class TariffZone(
    val gid: String? = null,
    val name: String? = null,
    val number: Int? = null,
    val shortName: String? = null,
)
