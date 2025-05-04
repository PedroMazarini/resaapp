package com.mazarini.resa.data.cache.service

import com.mazarini.resa.data.cache.database.SavedLocationDao
import com.mazarini.resa.data.cache.entities.SavedLocation
import com.mazarini.resa.data.cache.mappers.DomainToCacheSavedLocationMapper
import com.mazarini.resa.domain.model.Location
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class SavedLocationCacheServiceImplTest {
    private lateinit var dao: SavedLocationDao
    private lateinit var mapper: DomainToCacheSavedLocationMapper
    private lateinit var service: SavedLocationCacheServiceImpl

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        mapper = mockk(relaxed = true)
        service = SavedLocationCacheServiceImpl(dao, mapper)
    }

    @Test
    fun `saveLocation calls dao with mapped location`() = runTest {
        val domainLocation = mockk<Location>()
        val cacheLocation = mockk<SavedLocation>()
        coEvery { mapper.map(domainLocation) } returns cacheLocation
        service.saveLocation(domainLocation)
        coVerify { dao.insertLocation(cacheLocation) }
    }

    @Test
    fun `deleteLocation calls dao`() = runTest {
        val id = "id"
        service.deleteLocation(id)
        coVerify { dao.deleteLocationById(id) }
    }

    @Test
    fun `getAllLocations returns mapped flow`() = runTest {
        val cacheList = listOf(mockk<SavedLocation>())
        val domainList = listOf(mockk<Location>())
        coEvery { dao.getAllLocations() } returns flowOf(cacheList)
        coEvery { mapper.reverse(cacheList) } returns domainList
        val result = service.getAllLocations()
        // Then
        val collected = result.first()
        assertEquals(domainList, collected)
    }
}

