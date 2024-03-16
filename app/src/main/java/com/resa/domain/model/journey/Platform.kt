package com.resa.domain.model.journey

sealed class Platform {
    abstract val name: String
    data class Planned(override val name: String): Platform()
    data class Changed(override val name: String): Platform()
}