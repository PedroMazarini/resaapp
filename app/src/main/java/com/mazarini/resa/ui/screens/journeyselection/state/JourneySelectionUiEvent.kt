package com.mazarini.resa.ui.screens.journeyselection.state

import com.mazarini.resa.domain.model.journey.Direction
import com.mazarini.resa.domain.model.journey.Journey
import java.util.Date

sealed class JourneySelectionUiEvent {
    //Journey filter events
    data class IsDepartFilterChanged(val isDepart: Boolean) : JourneySelectionUiEvent()
    data class DateFilterChanged(val date: Date) : JourneySelectionUiEvent()
    data class TimeFilterChanged(val date: Date) : JourneySelectionUiEvent()
    data class JourneySelected(val journey: Journey) : JourneySelectionUiEvent()
    data class OnLegMapClicked(val direction: Direction) : JourneySelectionUiEvent()
    object SaveCurrentJourneySearch : JourneySelectionUiEvent()
    object TransportModesChanged : JourneySelectionUiEvent()
    object UpdateJourneySearch : JourneySelectionUiEvent()
}
