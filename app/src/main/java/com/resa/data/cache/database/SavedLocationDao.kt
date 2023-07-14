package com.resa.data.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.resa.data.cache.entities.SavedLocation as CacheLocation

@Dao
interface SavedLocationDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertLocation(location: CacheLocation): Long

    @Query("SELECT * FROM saved_locations  ORDER BY updatedAt DESC")
    fun getAllLocations(): Flow<List<CacheLocation>>

    @Query("DELETE FROM saved_locations WHERE id = :id")
    fun deleteLocationById(id: String)
}
