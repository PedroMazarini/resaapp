package com.resa.global.extensions

val Boolean?.orFalse: Boolean get() = this ?: false
val Boolean?.orTrue: Boolean get() = this ?: true

val String?.toIntOrZero: Int get() = this?.toIntOrNull() ?: 0