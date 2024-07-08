package com.mazarini.resa.data.cache.service

import com.mazarini.resa.data.cache.database.SavedJourneySearchDao
import com.mazarini.resa.data.cache.mappers.DomainToCacheSavedJourneySearchMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.mazarini.resa.domain.model.JourneySearch as DomainJourneySearch

interface SavedJourneySearchCacheService {
    suspend fun saveJourneySearch(journey: DomainJourneySearch)

    suspend fun deleteJourneySearch(id: String)

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

    override suspend fun deleteJourneySearch(id: String) =
        savedJourneySearchDao.deleteJourneyById(id)

    override suspend fun getAllJourneySearches(): Flow<List<DomainJourneySearch>> {
        return savedJourneySearchDao.getAllSavedJourneySearch().map {
            savedJourneyMapper.reverse(it)
        }
    }
}
