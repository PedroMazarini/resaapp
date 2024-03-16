package com.resa.ui.screens.journeydetails.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.resa.domain.model.journey.Journey
import com.resa.domain.model.journey.Leg
import com.resa.domain.model.journey.LegStop
import kotlinx.coroutines.flow.MutableStateFlow

data class JourneyDetailsUiState(
    val legStopList: MutableState<List<LegStop>> = mutableStateOf(listOf()),
    val selectedJourney: MutableStateFlow<Journey?> = MutableStateFlow(null),
    val legs: MutableState<List<Leg>> = mutableStateOf(listOf()),
    val shouldRecompose: MutableState<Boolean> = mutableStateOf(false),
)
