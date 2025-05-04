package com.mazarini.resa.data.cache.service

import com.mazarini.resa.data.cache.database.RecentJourneySearchDao
import com.mazarini.resa.data.cache.entities.RecentJourneySearch
import com.mazarini.resa.data.cache.mappers.DomainToCacheRecentJourneySearchMapper
import com.mazarini.resa.domain.model.JourneySearch
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class RecentJourneySearchCacheServiceImplTest {
    private lateinit var dao: RecentJourneySearchDao
    private lateinit var mapper: DomainToCacheRecentJourneySearchMapper
    private lateinit var service: RecentJourneySearchCacheServiceImpl

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        mapper = mockk(relaxed = true)
        service = RecentJourneySearchCacheServiceImpl(dao, mapper)
    }

    @Test
    fun `saveJourneySearch calls dao with mapped journey`() = runTest {
        val domainJourney = mockk<JourneySearch>()
        val cacheJourney = mockk<RecentJourneySearch>()
        coEvery { mapper.map(domainJourney) } returns cacheJourney
        service.saveJourneySearch(domainJourney)
        coVerify { dao.insertJourney(cacheJourney) }
    }

    @Test
    fun `clearOldJourneySearch calls dao`() = runTest {
        service.clearOldJourneySearch()
        coVerify { dao.clearOldData() }
    }

    @Test
    fun `deleteJourneySearch calls dao`() = runTest {
        val id = "id"
        service.deleteJourneySearch(id)
        coVerify { dao.deleteJourneyById(id) }
    }

    @Test
    fun `getAllJourneySearches returns mapped flow`() = runTest {
        val cacheList = listOf(mockk<RecentJourneySearch>())
        val domainList = listOf(mockk<JourneySearch>())
        coEvery { dao.getAllRecentJourneySearch() } returns flowOf(cacheList)
        coEvery { mapper.reverse(cacheList) } returns domainList
        val result = service.getAllJourneySearches()
        // Collect the first emission from the flow and assert
        val collected = result.first()
        assertEquals(domainList, collected)
    }
}
