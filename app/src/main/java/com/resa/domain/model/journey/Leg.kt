package com.resa.domain.model.journey

import com.resa.domain.model.TransportMode

sealed class Leg {
    abstract val index: Int
    abstract val durationInMinutes: Int
    abstract val transportMode: TransportMode

    data class Transport(
        override val index: Int,
        val name: String,
        override val transportMode: TransportMode,
        override val durationInMinutes: Int,
        val colors: LegColors,
    ) : Leg()

    data class AccessLink(
        override val index: Int,
        override val transportMode: TransportMode,
        override val durationInMinutes: Int,
    ) : Leg()
}