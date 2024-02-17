package com.resa.domain.model.stoparea

data class StopPoint(
    val gid: String,
    val name: String,
    val platform: String,
    val latitude: Double,
    val longitude: Double,
    val departures: List<StopJourney> = emptyList(),
    val arrivals: List<StopJourney> = emptyList(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StopPoint

        return gid == other.gid
    }

    override fun hashCode(): Int {
        // Custom hash code (e.g., based on the 'id' field)
        return gid.hashCode()
    }
}
