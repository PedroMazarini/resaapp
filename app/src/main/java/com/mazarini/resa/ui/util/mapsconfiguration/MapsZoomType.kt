package com.mazarini.resa.ui.util.mapsconfiguration

sealed class MapsZoomType {
    object Default : MapsZoomType()
    data class MyLocation(val lat: Double, val lon: Double): MapsZoomType()
}