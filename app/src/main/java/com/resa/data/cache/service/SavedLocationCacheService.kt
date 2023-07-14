package com.resa.data.cache.service

import com.resa.data.cache.database.SavedLocationDao
import com.resa.data.cache.mappers.DomainToCacheSavedLocationMapper
import com.resa.domain.model.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.resa.domain.model.Location as DomainLocation

interface SavedLocationCacheService {
    suspend fun saveLocation(location: DomainLocation)

    suspend fun deleteLocation(id: String)

    suspend fun getAllLocations(): Flow<List<DomainLocation>>
}

class SavedLocationCacheServiceImpl
@Inject
constructor(
    private val savedLocationDao: SavedLocationDao,
    private val locationMapper: DomainToCacheSavedLocationMapper,
) : SavedLocationCacheService {

    override suspend fun saveLocation(location: DomainLocation) {
        savedLocationDao.insertLocation(locationMapper.map(location))
    }

    override suspend fun deleteLocation(id: String) =
        savedLocationDao.deleteLocationById(id)

    override suspend fun getAllLocations(): Flow<List<Location>> {
        return savedLocationDao.getAllLocations().map {
            locationMapper.reverse(it)
        }
    }
}
