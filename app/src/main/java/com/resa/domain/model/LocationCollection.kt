package com.resa.domain.model

data class LocationCollection(
    val locations: List<Location>,
    val pagination: PaginationData,
)