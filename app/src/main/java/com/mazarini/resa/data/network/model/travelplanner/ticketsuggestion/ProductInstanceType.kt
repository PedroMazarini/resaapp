package com.mazarini.resa.data.network.model.travelplanner.ticketsuggestion

import com.squareup.moshi.JsonClass

/**
 * Specifies whether or not the product is dynamically based on the journey route.
 *
 * Values: static,`dynamic`
 */
@JsonClass(generateAdapter = false)
enum class ProductInstanceType {
    static,
    dynamic,
}
