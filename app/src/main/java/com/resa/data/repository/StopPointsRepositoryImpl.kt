package com.resa.data.repository

import com.resa.data.network.datasource.abstraction.StopPointsDatasource
import com.resa.data.network.mappers.DomainStopPoints
import com.resa.domain.model.Coordinate
import com.resa.domain.repositoryAbstraction.StopPointsRepository
import com.resa.global.preferences.PrefsProvider
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopPointsRepositoryImpl
@Inject
constructor(
    private val stopPointsDatasource: StopPointsDatasource,
    private val prefsProvider: PrefsProvider,
) : StopPointsRepository {

    override suspend fun getDeparturesAround(coordinate: Coordinate): Result<DomainStopPoints> =
        stopPointsDatasource.getDeparturesAround(
            coordinate = coordinate,
            token = prefsProvider.getToken(),
        )
}
