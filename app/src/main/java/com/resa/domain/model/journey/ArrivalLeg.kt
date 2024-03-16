package com.resa.domain.model.journey

sealed class ArrivalLeg {
    object None : ArrivalLeg()

    data class Details(
        val name: String,
        val arrivalTime: JourneyTimes,
    ) : ArrivalLeg()
}
