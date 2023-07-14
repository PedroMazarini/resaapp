package com.resa.global

interface Mapper<T, R> {
    fun map(value: T): R
    fun reverse(value: R): T =  throw NotImplementedError(message = "")
    fun map(valueList: List<T>) = valueList.map { map(it) }
    fun reverse(valueList: List<R>) = valueList.map { reverse(it) }
}