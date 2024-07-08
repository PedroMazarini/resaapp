package com.mazarini.resa.data.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mazarini.resa.domain.model.LocationType
import java.util.Date

@Entity(tableName = "saved_locations")
data class SavedLocation(

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
