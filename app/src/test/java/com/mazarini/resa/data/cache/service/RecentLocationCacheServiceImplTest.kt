package com.mazarini.resa.data.cache.service

import com.mazarini.resa.data.cache.database.RecentLocationDao
import com.mazarini.resa.data.cache.entities.RecentLocation
import com.mazarini.resa.data.cache.mappers.DomainToCacheRecentLocationMapper
import com.mazarini.resa.domain.model.Location
import com.mazarini.resa.domain.model.LocationType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class RecentLocationCacheServiceImplTest {
    private lateinit var dao: RecentLocationDao
    private lateinit var mapper: DomainToCacheRecentLocationMapper
    private lateinit var service: RecentLocationCacheServiceImpl

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        mapper = mockk(relaxed = true)
        service = RecentLocationCacheServiceImpl(dao, mapper)
    }

    @Test
    fun `saveLocation calls dao with mapped location`() = runTest {
        // Given
        val domainLocation = mockk<Location>()
        val cacheLocation = mockk<RecentLocation>()
        coEvery { mapper.map(domainLocation) } returns cacheLocation
        // When
        service.saveLocation(domainLocation)
        // Then
        coVerify { dao.insertLocation(cacheLocation) }
    }

    @Test
    fun `clearOldLocations calls dao`() = runTest {
        // When
        service.clearOldLocations()
        // Then
        coVerify { dao.clearOldData() }
    }

    @Test
    fun `getAllLocations returns mapped flow`() = runTest {
        // Given
        val cacheList = listOf(mockk<RecentLocation>())
        val domainList = listOf(mockk<Location>())
        coEvery { dao.getAllLocations(5, listOf(LocationType.stoparea)) } returns flowOf(cacheList)
        coEvery { mapper.reverse(cacheList) } returns domainList
        // When
        val result = service.getAllLocations(5, listOf(LocationType.stoparea))
        // Then
        val collected = result.first()
        assertEquals(domainList, collected)
    }
}

