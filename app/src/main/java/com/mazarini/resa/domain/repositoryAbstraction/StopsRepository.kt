package com.mazarini.resa.domain.repositoryAbstraction

import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.model.Location
import com.mazarini.resa.domain.model.stoparea.StopJourney

typealias DomainStopAreas = List<Location>

interface StopsRepository {

    suspend fun getStopsByCoordinate(coordinate: Coordinate): DomainStopAreas

    suspend fun getStopsByName(query: String): DomainStopAreas

    suspend fun getStopDepartures(stopAreaGid: String): List<StopJourney>
}