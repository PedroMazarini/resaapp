package com.resa.data.network.model.ticketsuggestion

import com.squareup.moshi.JsonClass

/**
 * Information about a channel related to the ticket suggestion.
 *
 * @param id The channel id.
 * @param ticketName The channel-specific ticket name, set if the channel is configured to override the default  product name.
 */
@JsonClass(generateAdapter = true)
data class Channel(
    val id: Int? = null,
    val ticketName: String? = null,
)
