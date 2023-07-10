package com.resa.domain.model

import com.squareup.moshi.JsonClass

/**
 * Values: unknown,none,tram,bus,ferry,train,taxi,walk,bike,car
 */
enum class TransportMode {
    unknown,
    none,
    tram,
    bus,
    ferry,
    train,
    taxi,
    walk,
    bike,
    car,
}
