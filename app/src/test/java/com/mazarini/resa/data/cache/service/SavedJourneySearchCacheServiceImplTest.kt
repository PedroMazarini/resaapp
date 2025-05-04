package com.mazarini.resa.data.cache.service

import com.mazarini.resa.data.cache.database.SavedJourneySearchDao
import com.mazarini.resa.data.cache.entities.SavedJourneySearch
import com.mazarini.resa.data.cache.mappers.DomainToCacheSavedJourneySearchMapper
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

class SavedJourneySearchCacheServiceImplTest {
    private lateinit var dao: SavedJourneySearchDao
    private lateinit var mapper: DomainToCacheSavedJourneySearchMapper
    private lateinit var service: SavedJourneySearchCacheServiceImpl

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        mapper = mockk(relaxed = true)
        service = SavedJourneySearchCacheServiceImpl(dao, mapper)
    }

    @Test
    fun `saveJourneySearch calls dao with mapped journey`() = runTest {
        val domainJourney = mockk<JourneySearch>()
        val cacheJourney = mockk<SavedJourneySearch>()
        coEvery { mapper.map(domainJourney) } returns cacheJourney
        service.saveJourneySearch(domainJourney)
        coVerify { dao.insertJourney(cacheJourney) }
    }

    @Test
    fun `deleteJourneySearch calls dao`() = runTest {
        val id = "id"
        service.deleteJourneySearch(id)
        coVerify { dao.deleteJourneyById(id) }
    }

    @Test
    fun `getAllJourneySearches returns mapped flow`() = runTest {
        val cacheList = listOf(mockk<SavedJourneySearch>())
        val domainList = listOf(mockk<JourneySearch>())
        coEvery { dao.getAllSavedJourneySearch() } returns flowOf(cacheList)
        coEvery { mapper.reverse(cacheList) } returns domainList
        val result = service.getAllJourneySearches()
        // Then
        val collected = result.first()
        assertEquals(domainList, collected)
    }
}

