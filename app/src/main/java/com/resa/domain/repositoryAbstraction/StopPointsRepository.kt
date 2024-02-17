package com.resa.domain.repositoryAbstraction

import com.resa.data.network.mappers.DomainStopPoints
import com.resa.domain.model.Coordinate
import kotlinx.coroutines.flow.Flow

interface StopPointsRepository {

    suspend fun getDeparturesAround(coordinate: Coordinate): Result<DomainStopPoints>
}