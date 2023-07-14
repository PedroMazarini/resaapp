package com.resa.data.repository

import androidx.annotation.WorkerThread
import com.resa.data.cache.service.RecentLocationCacheService
import com.resa.data.cache.service.SavedLocationCacheService
import com.resa.data.network.datasource.abstraction.LocationsDatasource
import com.resa.data.network.model.location.QueryLocationsParams
import com.resa.domain.model.Location
import com.resa.domain.repositoryAbstraction.LocationsRepository
import com.resa.global.preferences.PrefsProvider
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
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

    init {
        clearOldLocations()
    }

    @WorkerThread
    override suspend fun queryLocationByText(
        query: String,
    ) = locationsDatasource.queryLocationsByText(
        queryLocationsParams = QueryLocationsParams.ByText(
            query = query,
        ),
        token = prefsProvider.getToken(),
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

    @OptIn(DelicateCoroutinesApi::class)
    fun clearOldLocations() {
        GlobalScope.launch(Dispatchers.IO) {
            recentLocationCacheService.clearOldLocations()
        }
    }

    override suspend fun getAllRecentLocation(): Flow<List<Location>> =
        recentLocationCacheService.getAllLocations()

}
