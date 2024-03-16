package com.resa.domain.model.journey

import com.resa.domain.model.Coordinate

sealed class LegDetails {
    object None : LegDetails()
    data class Details(
        val index: Int,
        val platForm: Platform?,
        val pathWay: List<Coordinate>,
        val legStops: List<LegStop>,
    ) : LegDetails()

    val Details.departName: String
        get() = legStops.firstOrNull()?.name ?: ""

    val Details.arrivalName: String
        get() = legStops.lastOrNull()?.name ?: ""
    val Details.departTime: String
        get() = legStops.firstOrNull()?.time ?: ""
    val Details.arrivalTime: String
        get() = legStops.lastOrNull()?.time ?: ""
}
