package com.resa.ui.screens.home.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.resa.ui.model.JourneySearch

data class HomeUiState(
    var savedJourneys: MutableState<List<JourneySearch>> = mutableStateOf(listOf()),
)