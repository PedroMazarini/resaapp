package com.mazarini.resa.ui.model

data class JourneySearch(
    val id: String,
    val origin: Location,
    val destination: Location,
    val isLoading: Boolean = false,
)
