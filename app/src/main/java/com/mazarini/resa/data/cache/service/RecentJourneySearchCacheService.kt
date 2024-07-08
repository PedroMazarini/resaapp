package com.mazarini.resa.data.cache.service

import com.mazarini.resa.data.cache.database.RecentJourneySearchDao
import com.mazarini.resa.data.cache.mappers.DomainToCacheRecentJourneySearchMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.mazarini.resa.domain.model.JourneySearch as DomainJourneySearch

interface RecentJourneySearchCacheService {
    suspend fun saveJourneySearch(journey: DomainJourneySearch)

    suspend fun clearOldJourneySearch()

    suspend fun deleteJourneySearch(id: String)

    suspend fun getAllJourneySearches(): Flow<List<DomainJourneySearch>>
}

class RecentJourneySearchCacheServiceImpl
@Inject
constructor(
    private val recentJourneySearchDao: RecentJourneySearchDao,
    private val recentJourneyMapper: DomainToCacheRecentJourneySearchMapper,
) : RecentJourneySearchCacheService {

    override suspend fun saveJourneySearch(journey: DomainJourneySearch) {
        recentJourneySearchDao.insertJourney(recentJourneyMapper.map(journey))
    }

    override suspend fun clearOldJourneySearch() =
        recentJourneySearchDao.clearOldData()

    override suspend fun deleteJourneySearch(id: String) =
        recentJourneySearchDao.deleteJourneyById(id)

    override suspend fun getAllJourneySearches(): Flow<List<DomainJourneySearch>> {
        return recentJourneySearchDao.getAllRecentJourneySearch().map {
            recentJourneyMapper.reverse(it)
        }
    }
}
