package com.mazarini.resa.domain.model

data class ApiToken(
    val expireInSec: Int,
    val token: String,
)
