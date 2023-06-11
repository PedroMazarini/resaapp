package com.resa.data.network.model.ticketsuggestion

import com.squareup.moshi.JsonClass

/**
 * @param hasError Flag indicating that an error occurred while getting ticket suggestions.
 * @param ticketSuggestions Ticket suggestions for a journey.
 */
@JsonClass(generateAdapter = true)
data class JourneyTicketSuggestions(
    val hasError: Boolean? = null,
    val ticketSuggestions: List<TicketSuggestion>? = null,
)
