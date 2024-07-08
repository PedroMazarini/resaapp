package com.mazarini.resa.ui.screens.journeydetails.state

import com.mazarini.resa.domain.model.VehiclePosition
import com.mazarini.resa.domain.model.journey.Direction

sealed class JourneyDetailsUiEvent {

    data class OnLegMapClicked(val direction: Direction) : JourneyDetailsUiEvent()
    data class LocationAccessResult(val hasAccess: Boolean) : JourneyDetailsUiEvent()
    object OnBackPressed : JourneyDetailsUiEvent()

    object StartTrackingVehicles : JourneyDetailsUiEvent()
    data class SetShouldShowMap(val show: Boolean) : JourneyDetailsUiEvent()
    data class OnFollowClicked(val vehiclePosition: VehiclePosition) : JourneyDetailsUiEvent()
    object StopFollowingVehicle : JourneyDetailsUiEvent()
    object SaveJourneyToHome : JourneyDetailsUiEvent()
}
