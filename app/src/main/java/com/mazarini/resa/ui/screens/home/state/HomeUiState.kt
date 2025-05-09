package com.mazarini.resa.ui.screens.home.state

import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.global.model.ThemeSettings
import com.mazarini.resa.ui.model.JourneySearch
import com.mazarini.resa.ui.screens.home.model.SavedJourneyState
import com.mazarini.resa.ui.screens.home.model.StopPointsState
import com.mazarini.resa.ui.screens.home.state.HomeViewModel.*

data class HomeUiState(
    val savedJourneyState: SavedJourneyState = SavedJourneyState.Loading,
    val recentJourneysState: SavedJourneyState = SavedJourneyState.Loading,
    val navigateToJourneySelection: Boolean = false,
    val requestGpsLocation: Boolean = false,
    val hasLocationPermission: Boolean = false,
    val hasCheckedPermission: Boolean = false,
    val showOnboarding: Boolean = false,
    val journeySearchRequest: JourneySearch? = null,
    val stopPoints: StopPointsState = StopPointsState.Loading,
    val currentLocation: Coordinate? = null,
    val pinnedHomeJourney: Journey? = null,
    val pendingLocationUses: PendingLocationUse = PendingLocationUse.NONE,
    val isDeparturesReloading: Boolean = false,
    val currentLanguage: String = "",
    val currentTheme: ThemeSettings = ThemeSettings.SYSTEM,
)
