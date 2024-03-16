package com.resa.ui.screens.locationsearch.state

import com.resa.ui.model.Location
import java.util.Date


sealed class LocationSearchUiEvent {
    data class DeleteFavorite(val favoriteId: String) : LocationSearchUiEvent()

    //Search field events
    object SwapLocations : LocationSearchUiEvent()
    object ClearOrigin : LocationSearchUiEvent()
    object ClearDest : LocationSearchUiEvent()
    object NavigateToResults : LocationSearchUiEvent()
    data class RequestLocation(val currentLocation: CurrentLocation?) : LocationSearchUiEvent()
    data class LocationResult(val lat: Double, val lon: Double, val name: String) : LocationSearchUiEvent()
    data class OriginSearchChanged(val query: String) : LocationSearchUiEvent()
    data class DestSearchChanged(val query: String) : LocationSearchUiEvent()
    data class LocationSelected(val location: Location) : LocationSearchUiEvent()
    data class SaveLocation(val location: Location) : LocationSearchUiEvent()
    data class DeleteLocation(val id: String) : LocationSearchUiEvent()
    data class OriginFocusChanged(val hasFocus: Boolean) : LocationSearchUiEvent()
    data class DestFocusChanged(val hasFocus: Boolean) : LocationSearchUiEvent()

    //Journey filter events
    data class IsDepartFilterChanged(val isDepart: Boolean) : LocationSearchUiEvent()
    data class DateFilterChanged(val date: Date) : LocationSearchUiEvent()
    data class TimeFilterChanged(val date: Date) : LocationSearchUiEvent()
}
