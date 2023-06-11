package com.resa.global

interface Mapper<T, R> {
    fun map(value: T): R
    fun map(valueList: List<T>) = valueList.map { map(it) }
}