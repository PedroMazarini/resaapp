package com.mazarini.resa.data.cache.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mazarini.resa.data.cache.entities.SavedLocation
import com.mazarini.resa.domain.model.LocationType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SavedLocationDaoTest {
    private lateinit var db: ResaDatabase
    private lateinit var dao: SavedLocationDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ResaDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.savedLocationDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    private fun createSavedLocation(
        id: String,
        name: String,
        lat: Double?,
        lon: Double?,
        type: LocationType,
        updatedAt: Long
    ) = SavedLocation(
        id = id,
        name = name,
        lat = lat,
        lon = lon,
        type = type,
        updatedAt = updatedAt
    )

    @Test
    fun insertAndGetAllLocations() = runBlocking {
        val location =
            createSavedLocation("id1", "name", 1.0, 2.0, LocationType.stoparea, 123456789)
        dao.insertLocation(location)
        val result = dao.getAllLocations().first()
        assertEquals(listOf(location), result)
    }

    @Test
    fun deleteLocationById_removesLocation() = runBlocking {
        val location =
            createSavedLocation("id2", "name2", 3.0, 4.0, LocationType.stoparea, 123456789)
        dao.insertLocation(location)
        dao.deleteLocationById("id2")
        val result = dao.getAllLocations().first()
        assertEquals(emptyList<SavedLocation>(), result)
    }

    @Test
    fun insertMultipleAndGetAllLocations_ordersByUpdatedAtDesc() = runBlocking {
        val location1 = createSavedLocation("id1", "name1", 1.0, 2.0, LocationType.stoparea, 2000)
        val location2 = createSavedLocation("id2", "name2", 3.0, 4.0, LocationType.stoparea, 3000)
        val location3 = createSavedLocation("id3", "name3", 5.0, 6.0, LocationType.stoparea, 1000)
        dao.insertLocation(location1)
        dao.insertLocation(location2)
        dao.insertLocation(location3)
        val result = dao.getAllLocations().first()
        assertEquals(listOf(location2, location1, location3), result)
    }
}
