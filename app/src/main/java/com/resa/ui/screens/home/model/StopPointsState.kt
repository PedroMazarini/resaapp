package com.resa.ui.screens.home.model

import com.resa.domain.model.stoparea.StopPoint as DomainStopPoint
sealed class StopPointsState {
    object Loading : StopPointsState()

    object NeedLocation : StopPointsState()
    data class Loaded(val stopPoints: List<DomainStopPoint>) : StopPointsState()
    data class Error(val message: String) : StopPointsState()

    companion object {
        fun List<DomainStopPoint>.filterByDepartures(): List<DomainStopPoint> {
            return this.filter {
                it.departures.isNotEmpty()
            }.map {stopPoint ->
                stopPoint.copy(departures = stopPoint.departures.filterNot { it.time.hasDeparted() })
            }
        }
    }
}
