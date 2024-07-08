package com.mazarini.resa.global.extensions

fun <T> Collection<T>?.isNotNullOrEmpty(): Boolean = isNullOrEmpty().not()
