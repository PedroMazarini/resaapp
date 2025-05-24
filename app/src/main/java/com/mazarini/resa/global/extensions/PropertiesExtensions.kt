package com.mazarini.resa.global.extensions

val Boolean?.orFalse: Boolean get() = this ?: false
val Boolean?.orTrue: Boolean get() = this ?: true

val Any?.isNull: Boolean get() = this == null

val Any?.isNotNull: Boolean get() = this != null