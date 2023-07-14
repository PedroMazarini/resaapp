package com.resa.data.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.resa.data.cache.entities.RecentLocation as CacheLocation

@Dao
interface RecentLocationDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertLocation(location: CacheLocation): Long

    @Query("SELECT * FROM recent_locations ORDER BY updatedAt DESC")
    fun getAllLocations(): Flow<List<CacheLocation>>

    @Query("DELETE FROM recent_locations where id NOT IN " +
            "(SELECT id from recent_locations ORDER BY updatedAt DESC LIMIT 100)")
    fun clearOldData()
}
