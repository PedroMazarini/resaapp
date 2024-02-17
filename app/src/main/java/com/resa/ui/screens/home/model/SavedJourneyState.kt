package com.resa.ui.screens.home.model

import com.resa.ui.model.JourneySearch

sealed class SavedJourneyState {
    object Empty : SavedJourneyState()

    object Loading : SavedJourneyState()
    data class Loaded(val journeys: List<JourneySearch>) : SavedJourneyState()
    data class Error(val exception: Exception) : SavedJourneyState()
}
