package com.resa.ui.screens.journeyqueryresult.state

import kotlinx.coroutines.flow.MutableStateFlow

data class JourneyQueryResultUiState(
    val test: MutableStateFlow<String> = MutableStateFlow(""),
)
