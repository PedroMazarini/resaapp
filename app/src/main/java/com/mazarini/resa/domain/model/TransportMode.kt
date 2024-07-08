package com.mazarini.resa.domain.model

import androidx.annotation.StringRes
import com.mazarini.resa.R

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

        @StringRes
        fun TransportMode.stringRes(): Int =
            when(this) {
                unknown -> R.string.unknown
                none -> R.string.none
                tram -> R.string.tram
                bus -> R.string.bus
                ferry -> R.string.ferry
                train -> R.string.train
                taxi -> R.string.taxi
                walk -> R.string.walk
                bike -> R.string.bike
                car -> R.string.car
            }

        fun selectableModes() =
            listOf(bus, tram, ferry, train)
    }
}
