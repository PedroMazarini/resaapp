package com.mazarini.resa.ui.screens.home.state

import com.mazarini.resa.ui.model.JourneySearch
import com.mazarini.resa.ui.screens.home.state.HomeViewModel.PendingLocationUse

sealed class HomeUiEvent {

    data class OnSavedJourneyClicked(val journeySearch: JourneySearch) : HomeUiEvent()
    data class DeleteSavedJourney(val id: String) : HomeUiEvent()

    data class DeleteRecentJourney(val id: String) : HomeUiEvent()

    object NavigationRequested : HomeUiEvent()
    object LoadSavedJourneyToHome : HomeUiEvent()
    object DeleteSavedJourneyToHome : HomeUiEvent()
    object CheckSavedJourneyToHome : HomeUiEvent()

    object ClearLoadingSavedJourneys : HomeUiEvent()

    object RefreshDepartures : HomeUiEvent()
    data class UpdateGpsRequest(
        val request: Boolean,
        val pendingLocationUse: PendingLocationUse? = null,
    ) : HomeUiEvent()

    data class CheckedPermission(val hasPermission: Boolean) : HomeUiEvent()
    data class LocationResult(val lat: Double, val lon: Double) : HomeUiEvent()
}
