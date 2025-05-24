package com.mazarini.resa.ui.screens.journeydetails.state

import com.mazarini.resa.domain.model.VehiclePosition
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.domain.model.journey.LegStop

data class JourneyDetailsUiState(
    val legStopList: List<LegStop> = listOf(),
    val selectedJourney: Journey? = null,
    val hasLocationAccess: Boolean = false,
    val isTrackingAvailable: Boolean = false,
    val shouldShowMap: Boolean = false,
    val followingVehicle: VehiclePosition? = null,
    val hasCheckedForLocationAccess: Boolean = false,
    val trackedVehicles: List<VehiclePosition> = listOf(),
)
