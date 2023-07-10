package com.resa.domain.model

import com.squareup.moshi.JsonClass

/**
 * Values: unknown,none,vasttagen,longdistancetrain,regionaltrain
 */
enum class TransportSubMode {
    unknown,
    none,
    vasttagen,
    longdistancetrain,
    regionaltrain,
}
