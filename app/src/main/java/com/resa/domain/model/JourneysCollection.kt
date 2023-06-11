package com.resa.domain.model

data class JourneysCollection(
    val journeys: List<Journey>,
    val pagination: PaginationData,
)