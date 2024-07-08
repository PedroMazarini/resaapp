package com.mazarini.resa.data.repository

import com.mazarini.resa.data.network.datasource.abstraction.StopsDatasource
import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.model.stoparea.StopJourney
import com.mazarini.resa.domain.repositoryAbstraction.DomainStopAreas
import com.mazarini.resa.domain.repositoryAbstraction.StopsRepository
import com.mazarini.resa.global.preferences.PrefsProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopsRepositoryImpl
@Inject
constructor(
    private val stopsDatasource: StopsDatasource,
    private val prefsProvider: PrefsProvider,
) : StopsRepository {

    override suspend fun getStopsByCoordinate(coordinate: Coordinate): DomainStopAreas =
        try {
            stopsDatasource.getStopsByCoordinate(
                coordinate = coordinate,
                token = prefsProvider.getToken(),
            )
        } catch (e: Exception) {
            emptyList()
        }

    override suspend fun getStopsByName(query: String): DomainStopAreas =
        try {
            stopsDatasource.getStopsByName(
                query = query,
                token = prefsProvider.getToken(),
            )
        } catch (e: Exception) {
            emptyList()
        }

    override suspend fun getStopDepartures(stopAreaGid: String): List<StopJourney> =
        try {
            stopsDatasource.getStopDepartures(
                stopAreaGid = stopAreaGid,
                token = prefsProvider.getToken(),
            )
        } catch (e: Exception) {
            emptyList()
        }
}
