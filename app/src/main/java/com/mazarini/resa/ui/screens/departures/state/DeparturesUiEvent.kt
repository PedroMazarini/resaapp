package com.mazarini.resa.ui.screens.departures.state

import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.ui.model.Location
import com.mazarini.resa.ui.screens.locationsearch.state.CurrentLocation

sealed class DeparturesUiEvent {
    data class OnQueryChanged(val query: String): DeparturesUiEvent()
    data class QueryByCoordinate(val coordinate: Coordinate, val locationName: String): DeparturesUiEvent()
    data class OnStopSelected(val location: Location): DeparturesUiEvent()
    data class UserLocationRequest(val currentLocation: CurrentLocation?): DeparturesUiEvent()
    data class PinLocation(val location: Location): DeparturesUiEvent()
    object PullRefresh: DeparturesUiEvent()
    object DeleteCurrentPreferred: DeparturesUiEvent()
    object StartLocationRequest: DeparturesUiEvent()
    object LocationRequestOngoing: DeparturesUiEvent()
}
