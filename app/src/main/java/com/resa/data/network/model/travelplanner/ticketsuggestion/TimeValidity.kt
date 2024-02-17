package com.resa.data.network.model.travelplanner.ticketsuggestion

import com.squareup.moshi.JsonClass

/**
 * Information about the time validity of a ticket suggestion.
 *
 * @param type
 * @param amount The amount of the unit specified by the Unit property. Always used together with the Unit property.
 * @param unit
 * @param fromDate The from date of a date interval specified in RFC 3339 format. Always used together with the  ToDate property.
 * @param toDate The to date of a date interval specified in RFC 3339 format. Always used together with the  FromDate property.
 * @param fromDateTime The from time of a datetime interval specified in RFC 3339 format. Always used together with  the ToDateTime property.
 * @param toDateTime The to time of a datetime interval specified in RFC 3339 format. Always used together with  the FromDateTime property.
 */
@JsonClass(generateAdapter = true)
data class TimeValidity(
    val type: TimeValidityType? = null,
    val amount: Int? = null,
    val unit: TimeValidityUnit? = null,
    val fromDate: String? = null,
    val toDate: String? = null,
    val fromDateTime: String? = null,
    val toDateTime: String? = null,
)
