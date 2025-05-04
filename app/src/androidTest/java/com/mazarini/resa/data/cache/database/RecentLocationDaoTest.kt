package com.mazarini.resa.data.cache.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mazarini.resa.data.cache.entities.RecentLocation
import com.mazarini.resa.domain.model.LocationType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecentLocationDaoTest {
    private lateinit var db: ResaDatabase
    private lateinit var dao: RecentLocationDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ResaDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.recentLocationDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAndGetAllLocations_withTypesAndLimit() = runBlocking {
        val location1 = RecentLocation("id1", "name1", 1.0, 2.0, LocationType.stoparea, 2000)
        val location2 = RecentLocation("id2", "name2", 3.0, 4.0, LocationType.stoparea, 3000)
        val location3 = RecentLocation("id3", "name3", 5.0, 6.0, LocationType.stoppoint, 1000)
        dao.insertLocation(location1)
        dao.insertLocation(location2)
        dao.insertLocation(location3)
        val result = dao.getAllLocations(2, listOf(LocationType.stoparea)).first()
        assertEquals(listOf(location2, location1), result)
    }

    @Test
    fun clearOldData_removesOldLocations() = runBlocking {
        for (i in 1..105) {
            val location = RecentLocation("id$i", "name$i", 1.0, 2.0, LocationType.stoparea, i.toLong())
            dao.insertLocation(location)
        }
        dao.clearOldData()
        val result = dao.getAllLocations(200, listOf(LocationType.stoparea)).first()
        assertEquals(100, result.size)
        assertEquals("id105", result[0].id)
        assertEquals("id6", result.last().id)
    }
}
