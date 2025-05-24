package com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state

import com.mazarini.resa.domain.model.TransportMode
import java.util.Date

sealed class JourneyFilterUiEvent {
    data class OnReferenceChanged(val isDepart: Boolean): JourneyFilterUiEvent()
    data class OnDateChanged(val date: Date): JourneyFilterUiEvent()
    data class OnTimeChanged(val date: Date): JourneyFilterUiEvent()
    object OnModesClicked: JourneyFilterUiEvent()
    data class OnModesChanged(val modes: List<TransportMode>): JourneyFilterUiEvent()
}