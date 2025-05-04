package com.mazarini.resa.data.cache.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mazarini.resa.data.cache.entities.SavedJourneySearch
import com.mazarini.resa.domain.model.LocationType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SavedJourneySearchDaoTest {
    private lateinit var db: ResaDatabase
    private lateinit var dao: SavedJourneySearchDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ResaDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.savedJourneySearchDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    private fun createSavedJourneySearch(id: String, lastModified: Long) = SavedJourneySearch(
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
    fun insertAndGetAllSavedJourneySearch() = runBlocking {
        val journey = createSavedJourneySearch("jid1", 1000)
        dao.insertJourney(journey)
        val result = dao.getAllSavedJourneySearch().first()
        assertEquals(listOf(journey), result)
    }

    @Test
    fun deleteJourneyById_removesJourney() = runBlocking {
        val journey = createSavedJourneySearch("jid2", 2000)
        dao.insertJourney(journey)
        dao.deleteJourneyById("jid2")
        val result = dao.getAllSavedJourneySearch().first()
        assertEquals(emptyList<SavedJourneySearch>(), result)
    }

    @Test
    fun clearOldData_removesOldJourneys() = runBlocking {
        for (i in 1..55) {
            val journey = createSavedJourneySearch("jid$i", i.toLong())
            dao.insertJourney(journey)
        }
        dao.clearOldData()
        val result = dao.getAllSavedJourneySearch().first()
        assertEquals(50, result.size)
        assertEquals("jid55", result[0].id)
        assertEquals("jid6", result.last().id)
    }
}
