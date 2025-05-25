package com.mazarini.resa.data.repository

import androidx.annotation.WorkerThread
import com.mazarini.resa.data.cache.service.RecentLocationCacheService
import com.mazarini.resa.data.cache.service.SavedLocationCacheService
import com.mazarini.resa.data.network.datasource.abstraction.LocationsDatasource
import com.mazarini.resa.data.network.model.travelplanner.location.QueryLocationsParams
import com.mazarini.resa.domain.model.Location
import com.mazarini.resa.domain.model.LocationType
import com.mazarini.resa.domain.repositoryAbstraction.LocationsRepository
import com.mazarini.resa.global.preferences.PrefsProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationsRepositoryImpl
@Inject
constructor(
    private val locationsDatasource: LocationsDatasource,
    private val prefsProvider: PrefsProvider,
    private val savedLocationCacheService: SavedLocationCacheService,
    private val recentLocationCacheService: RecentLocationCacheService,
) : LocationsRepository {

    @WorkerThread
    override suspend fun queryLocationByText(
        query: String,
    ) = locationsDatasource.queryLocationsByText(
        queryLocationsParams = QueryLocationsParams.ByText(
            query = query,
        ),
        token = prefsProvider.getToken().token,
    ).flowOn(Dispatchers.IO)

    override suspend fun saveLocation(location: Location) {
        savedLocationCacheService.saveLocation(location)
    }

    override suspend fun deleteSavedLocation(id: String) {
        savedLocationCacheService.deleteLocation(id)
    }

    override suspend fun getAllSavedLocation(): Flow<List<Location>> =
        savedLocationCacheService.getAllLocations()

    override suspend fun saveRecentLocation(location: Location) {
        recentLocationCacheService.saveLocation(location)
    }

    override suspend fun clearOldLocations() {
        recentLocationCacheService.clearOldLocations()
    }

    override suspend fun getAllRecentLocation(
        limit: Int,
        types: List<LocationType>,
    ): Flow<List<Location>> =
        recentLocationCacheService.getAllLocations(
            limit = limit,
            types = types,
        )

}
