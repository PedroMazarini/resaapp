package com.resa.data.cache.service

import com.resa.data.cache.database.RecentLocationDao
import com.resa.data.cache.mappers.DomainToCacheRecentLocationMapper
import com.resa.domain.model.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.resa.domain.model.Location as DomainLocation

interface RecentLocationCacheService {
    suspend fun saveLocation(location: DomainLocation)

    suspend fun clearOldLocations()

    suspend fun getAllLocations(): Flow<List<DomainLocation>>
}

class RecentLocationCacheServiceImpl
@Inject
constructor(
    private val recentLocationDao: RecentLocationDao,
    private val locationMapper: DomainToCacheRecentLocationMapper,
) : RecentLocationCacheService {

    override suspend fun saveLocation(location: DomainLocation) {
        recentLocationDao.insertLocation(locationMapper.map(location))
    }

    override suspend fun clearOldLocations() =
        recentLocationDao.clearOldData()

    override suspend fun getAllLocations(): Flow<List<Location>> {
        return recentLocationDao.getAllLocations().map {
            locationMapper.reverse(it)
        }
    }
}
