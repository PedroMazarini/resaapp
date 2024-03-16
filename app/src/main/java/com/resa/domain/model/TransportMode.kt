package com.resa.domain.model

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
    car;

    companion object {
        fun TransportMode.isWalkOrBike(): Boolean {
            return this == walk || this == bike
        }
    }
}
