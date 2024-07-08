package com.mazarini.resa.ui.screens.home.model

sealed class StopPointsState {
    object Loading : StopPointsState()

    object NeedLocation : StopPointsState()
//    data class Loaded(val stopPoints: List<DomainStopPoint>) : StopPointsState()
    data class Error(val message: String) : StopPointsState()
}
