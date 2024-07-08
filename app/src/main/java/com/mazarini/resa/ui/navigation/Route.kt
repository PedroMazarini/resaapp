package com.mazarini.resa.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    object Home : Route()
    @Serializable
    data class LocationSearch(val focusRequest: String? = null) : Route()
    @Serializable
    object JourneySelection : Route()
    @Serializable
    object JourneyDetails : Route()
    @Serializable
    object Departures : Route()
    @Serializable
    object About : Route()
    @Serializable
    object Back : Route()
    @Serializable
    object Onboarding : Route()
}
