package com.mazarini.resa.data.network.model.geography.stopareas.response

/**
 * 
 *
 * @param northingCoordinate Points northing coordinate.
 * @param eastingCoordinate Points easting coordinate.
 * @param wkt Format to describe geometry.
 * @param srid Unique reference code for coordinate system.
 * @param coordinateSystemName Name of coordinate system.
 */
data class Point (
    val northingCoordinate: Double? = null,
    val eastingCoordinate: Double? = null,
    val wkt: String? = null,
    val srid: Int? = null,
    val coordinateSystemName: String? = null,
)
