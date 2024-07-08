package com.mazarini.resa.data.network.datasource.abstraction

import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.model.stoparea.StopJourney
import com.mazarini.resa.domain.repositoryAbstraction.DomainStopAreas

interface StopsDatasource {

    suspend fun getStopsByCoordinate(
        coordinate: Coordinate,
        token: String,
    ): DomainStopAreas

    suspend fun getStopsByName(
        query: String,
        token: String,
    ): DomainStopAreas

    suspend fun getStopDepartures(stopAreaGid: String, token: String): List<StopJourney>
}
