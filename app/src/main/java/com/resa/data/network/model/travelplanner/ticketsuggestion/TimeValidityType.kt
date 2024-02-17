package com.resa.data.network.model.travelplanner.ticketsuggestion

import com.squareup.moshi.JsonClass

/**
 * Values: unknown,amountunit,fromtodate,fromtodatetime
 */
@JsonClass(generateAdapter = false)
enum class TimeValidityType {
    unknown,
    amountunit,
    fromtodate,
    fromtodatetime,
}
