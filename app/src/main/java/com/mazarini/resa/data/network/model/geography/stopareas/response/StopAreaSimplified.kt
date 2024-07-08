package com.mazarini.resa.data.network.model.geography.stopareas.response

/**
 * 
 *
 * @param gid GID for stop area.
 * @param number Number for stop area.
 * @param name Name of stop area.
 */
data class StopAreaSimplified (
    val gid: String? = null,
    val number: Int? = null,
    val name: String? = null,
)
