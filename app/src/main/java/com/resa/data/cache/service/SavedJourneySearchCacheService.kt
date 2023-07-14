package com.resa.data.cache.service

import com.resa.data.cache.database.SavedJourneySearchDao
import com.resa.data.cache.mappers.DomainToCacheSavedJourneySearchMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.resa.domain.model.JourneySearch as DomainJourneySearch

interface SavedJourneySearchCacheService {
    suspend fun saveJourneySearch(journey: DomainJourneySearch)

    suspend fun deleteJourneySearch(id: Int)

    suspend fun getAllJourneySearches(): Flow<List<DomainJourneySearch>>
}

class SavedJourneySearchCacheServiceImpl
@Inject
constructor(
    private val savedJourneySearchDao: SavedJourneySearchDao,
    private val savedJourneyMapper: DomainToCacheSavedJourneySearchMapper,
) : SavedJourneySearchCacheService {

    override suspend fun saveJourneySearch(journey: DomainJourneySearch) {
        savedJourneySearchDao.insertJourney(savedJourneyMapper.map(journey))
    }

    override suspend fun deleteJourneySearch(id: Int) =
        savedJourneySearchDao.deleteJourneyById(id)

    override suspend fun getAllJourneySearches(): Flow<List<DomainJourneySearch>> {
        return savedJourneySearchDao.getAllSavedJourneySearch().map {
            savedJourneyMapper.reverse(it)
        }
    }
}
