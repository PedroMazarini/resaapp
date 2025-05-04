package com.mazarini.resa.data.cache.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mazarini.resa.data.cache.entities.RecentJourneySearch
import com.mazarini.resa.domain.model.LocationType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecentJourneySearchDaoTest {
    private lateinit var db: ResaDatabase
    private lateinit var dao: RecentJourneySearchDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ResaDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.recentJourneySearchDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    private fun createRecentJourneySearch(
        id: String,
        lastModified: Long
    ) = RecentJourneySearch(
        id = id,
        originId = "oid$id",
        originName = "oname$id",
        originLat = 1.0,
        originLon = 2.0,
        originType = LocationType.stoparea,
        destinationId = "did$id",
        destinationName = "dname$id",
        destinationLat = 3.0,
        destinationLon = 4.0,
        destinationType = LocationType.stoparea,
        lastModified = lastModified
    )

    @Test
    fun insertAndGetAllRecentJourneySearch() = runBlocking {
        val journey = createRecentJourneySearch("jid1", 1000)
        dao.insertJourney(journey)
        val result = dao.getAllRecentJourneySearch().first()
        assertEquals(listOf(journey), result)
    }

    @Test
    fun deleteJourneyById_removesJourney() = runBlocking {
        val journey = createRecentJourneySearch("jid2", 2000)
        dao.insertJourney(journey)
        dao.deleteJourneyById("jid2")
        val result = dao.getAllRecentJourneySearch().first()
        assertEquals(emptyList<RecentJourneySearch>(), result)
    }

    @Test
    fun clearOldData_removesOldJourneys() = runBlocking {
        for (i in 1..55) {
            val journey = createRecentJourneySearch("jid$i", i.toLong())
            dao.insertJourney(journey)
        }
        dao.clearOldData()
        val result = dao.getAllRecentJourneySearch().first()
        assertEquals(50, result.size)
        assertEquals("jid55", result[0].id)
        assertEquals("jid6", result.last().id)
    }
}
