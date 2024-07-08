package com.mazarini.resa.data.network.model.travelplanner.ticketsuggestion

import com.squareup.moshi.JsonClass

/**
 * Information about a zone related to the ticket suggestion.
 *
 * @param id The zone id.
 */
@JsonClass(generateAdapter = true)
data class Zone(val id: Int? = null)
