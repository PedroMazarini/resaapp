package com.mazarini.resa.domain.model

data class JourneySearch(
    val id: String,
    val origin: Location,
    val destination: Location,
)
