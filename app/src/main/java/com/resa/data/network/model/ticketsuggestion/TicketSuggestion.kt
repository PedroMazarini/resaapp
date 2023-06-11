package com.resa.data.network.model.ticketsuggestion

import com.squareup.moshi.JsonClass

/**
 * Information about a ticket suggestion.
 *
 * @param productId The product id.
 * @param productName The product name.
 * @param productType The product type.
 * @param travellerCategory
 * @param priceInSek The product price in SEK.
 * @param timeValidity
 * @param timeLimitation
 * @param saleChannels A list of the channels that sell the product.
 * @param validZones A list of the valid zones for the ticket.
 * @param productInstanceType
 * @param punchConfiguration
 * @param offerSpecification Used to get ticket offer.
 */
@JsonClass(generateAdapter = true)
data class TicketSuggestion(
    val productId: Int? = null,
    val productName: String? = null,
    val productType: Int? = null,
    val travellerCategory: TravellerCategory? = null,
    val priceInSek: Double? = null,
    val timeValidity: TimeValidity? = null,
    val timeLimitation: TimeLimitation? = null,
    val saleChannels: List<Channel>? = null,
    val validZones: List<Zone>? = null,
    val productInstanceType: ProductInstanceType? = null,
    val punchConfiguration: PunchConfiguration? = null,
    val offerSpecification: String? = null,
)
