package com.mazarini.resa.data.cache.service

import com.mazarini.resa.data.cache.database.RecentLocationDao
import com.mazarini.resa.data.cache.mappers.DomainToCacheRecentLocationMapper
import com.mazarini.resa.domain.model.Location
import com.mazarini.resa.domain.model.LocationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.mazarini.resa.domain.model.Location as DomainLocation

interface RecentLocationCacheService {
    suspend fun saveLocation(location: DomainLocation)

    suspend fun clearOldLocations()

    suspend fun getAllLocations(
        limit: Int,
        types: List<LocationType>,
    ): Flow<List<DomainLocation>>
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

    override suspend fun getAllLocations(
        limit: Int,
        types: List<LocationType>,
    ): Flow<List<Location>> {
        return recentLocationDao.getAllLocations(
            limit = limit,
            types = types,
        ).map {
            locationMapper.reverse(it)
        }
    }
}
