package com.resa.global.extensions

import java.util.Calendar
import java.util.Date

fun Date.plusSec(sec: Int): Date {
    val now = Calendar.getInstance()
    now.add(Calendar.SECOND, sec)
    return now.time
}

fun Date.hasExpired(toleranceInMin: Int): Boolean {
    val now = Calendar.getInstance()
    now.add(Calendar.MINUTE, toleranceInMin)
    return this.before(now.time)
}