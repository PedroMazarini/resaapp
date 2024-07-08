package com.mazarini.resa.data.network.model.geography.stopareas.response

/**
 * 
 *
 * @param transportAuthorityName Number for transport authority.
 * @param transportAuthorityCode Name of transport authority.
 * @param transportAuthorityNumber Code for transport authority.
 */
data class TransportAuthority (
    val transportAuthorityName: String? = null,
    val transportAuthorityCode: String? = null,
    val transportAuthorityNumber: Int? = null,
)
