package com.resa.ui.screens.journeydetails.state

import com.resa.domain.model.journey.Leg
import com.resa.ui.screens.journeyselection.state.JourneySelectionUiEvent

sealed class JourneyDetailsUiEvent {

    data class OnLegMapClicked(val leg: Leg) : JourneyDetailsUiEvent()
    object OnBackPressed : JourneyDetailsUiEvent()
}
