package com.mazarini.resa.data.network.model.travelplanner.location

sealed class QueryLocationsParams {

    abstract val types: List<QueryLocationType>?
    abstract val limit: Int?
    abstract val offset: Int?

    /**
     * The parameters to search for a Location by text.
     *
     * @param query the text used to search a location.
     * @param types the location types to search for, default is ALL locations.
     * @param limit the maximum number of results to return, default is 10.
     * @param offset the offset of the first result to return, default is 0.
     */
    data class ByText(
        val query: String,
        override val types: List<QueryLocationType>? = null,
        override val limit: Int? = null,
        override val offset: Int? = null,
    ) : QueryLocationsParams()

    /**
     * The parameters to search for a Location by coordinates.
     *
     * @param latitude the latitude used to search a location.
     * @param longitude the longitude used to search a location.
     * @param radiusInMeters the radius around the coordinates, default is 500.
     * @param types the location types to search for, default is ALL locations.
     * @param limit the maximum number of results to return, default is 10.
     * @param offset the offset of the first result to return, default is 0.
     */
    data class ByCoordinates(
        val latitude: Double,
        val longitude: Double,
        val radiusInMeters: Int? = null,
        override val types: List<QueryLocationType>? = null,
        override val limit: Int? = null,
        override val offset: Int? = null,
    ) : QueryLocationsParams()

    val queryMode: String
        get() = when (this) {
            is ByText -> "by-text"
            is ByCoordinates -> "by-coordinates"
        }
}

fun QueryLocationsParams.typesNames(): List<String> {
    return types?.map { it.name } ?: emptyList()
}

enum class QueryLocationType {
    stoparea,
    address,
    pointofinterest,
    metastation,
}