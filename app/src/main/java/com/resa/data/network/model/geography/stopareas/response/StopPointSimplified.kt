package com.resa.data.network.model.geography.stopareas.response

/**
 *
 *
 * @param gid GID for stop point.
 * @param number Number for stop point.
 * @param name Name of stop point.
 * @param shortName Short name for stop point.
 */

data class StopPointSimplified (
    val gid: String? = null,
    val number: Int? = null,
    val name: String? = null,
    val shortName: String? = null,
)