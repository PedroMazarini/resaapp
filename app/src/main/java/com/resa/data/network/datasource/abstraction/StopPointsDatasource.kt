package com.resa.data.network.datasource.abstraction

import com.resa.domain.model.Coordinate
import com.resa.domain.model.stoparea.StopPoint
import kotlinx.coroutines.flow.Flow

typealias DomainStopPoints = List<StopPoint>
interface StopPointsDatasource {

    suspend fun getDeparturesAround(
        coordinate: Coordinate,
        token: String,
    ): Result<DomainStopPoints>
}
