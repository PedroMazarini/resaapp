package com.resa.ui.screens.journeyselection.state

import java.util.Date

sealed class JourneySelectionUiEvent {
    //Journey filter events
    data class IsDepartFilterChanged(val isDepart: Boolean) : JourneySelectionUiEvent()
    data class DateFilterChanged(val date: Date) : JourneySelectionUiEvent()
    data class TimeFilterChanged(val date: Date) : JourneySelectionUiEvent()
    object SaveCurrentJourneySearch : JourneySelectionUiEvent()
}
