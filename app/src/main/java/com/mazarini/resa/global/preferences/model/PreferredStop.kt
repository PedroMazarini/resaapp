package com.mazarini.resa.global.preferences.model

import kotlinx.serialization.Serializable

@Serializable
data class PreferredStop(
    val gid: String,
    val name: String,
)