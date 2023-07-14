package com.resa.data.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.resa.domain.model.LocationType
import java.util.Date

@Entity(tableName = "recent_locations")
data class RecentLocation(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "lat")
    var lat: Double?,

    @ColumnInfo(name = "lon")
    var lon: Double?,

    @ColumnInfo(name = "type")
    var type: LocationType,

    @ColumnInfo(name = "updatedAt")
    var updatedAt: Long = Date().time,
)
