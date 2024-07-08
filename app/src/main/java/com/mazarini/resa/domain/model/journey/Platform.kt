package com.mazarini.resa.domain.model.journey

import kotlinx.serialization.Serializable

@Serializable
sealed class Platform {
    abstract val name: String

    @Serializable
    data class Planned(override val name: String) : Platform()

    @Serializable
    data class Changed(
        override val name: String,
        val oldName: String,
    ) : Platform()
}