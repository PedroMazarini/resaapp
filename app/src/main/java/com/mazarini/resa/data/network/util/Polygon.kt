package com.mazarini.resa.data.network.util

import com.mazarini.resa.domain.model.Coordinate
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

object Polygon {

    fun createSquarePolygon(coordinate: Coordinate, radius: Double = 150.0): String {
        val earthRadius = 6371000.0 // Earth's radius in meters

        // Convert latitude and longitude to radians
        val latRad = coordinate.lat.toRadians()
        val lonRad = coordinate.lon.toRadians()

        // Calculate the angular distance
        val angDist = radius / earthRadius

        val points = mutableListOf<Pair<Double, Double>>()

        // Calculate the points on the square
        val northLat = asin(sin(latRad) * cos(angDist) + cos(latRad) * sin(angDist))
        val southLat = asin(sin(latRad) * cos(angDist) - cos(latRad) * sin(angDist))
        val eastLon = lonRad + atan2(sin(90.0.toRadians()) * sin(angDist) * cos(latRad), cos(angDist) - sin(latRad) * sin(northLat))
        val westLon = lonRad + atan2(sin(270.0.toRadians()) * sin(angDist) * cos(latRad), cos(angDist) - sin(latRad) * sin(southLat))

        points.add(Math.toDegrees(latRad) to Math.toDegrees(westLon))
        points.add(Math.toDegrees(northLat) to Math.toDegrees(lonRad))
        points.add(Math.toDegrees(latRad) to Math.toDegrees(eastLon))
        points.add(Math.toDegrees(southLat) to Math.toDegrees(lonRad))
        points.add(Math.toDegrees(latRad) to Math.toDegrees(westLon))

        // Format the points in WKT format
        val polygon = StringBuilder("POLYGON ((")
        points.forEachIndexed { index, point ->
            polygon.append("${point.second} ${point.first}")
            if (index != points.size - 1)
                polygon.append(", ")
        }
        polygon.append("))")

        return polygon.toString()
    }

    private fun Double.toRadians(): Double {
        return Math.toRadians(this)
    }
}