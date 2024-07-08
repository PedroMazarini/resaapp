package com.mazarini.resa.data.network.model.geography.stopareas.response

/**
 * 
 *
 * @param wkt Format to describe geometry of the polygon.
 * @param srid Unique reference code for coordinate system of the polygon.
 * @param coordinateSystemName Name of coordinate system for the polygon.
 */
data class Polygon (
    val wkt: String? = null,
    val srid: Int? = null,
    val coordinateSystemName: String? = null,
)
