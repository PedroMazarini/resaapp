package com.mazarini.resa.data.network.requestHandlers

/**
 * A generic class that holds a value or an exception
 */
sealed class DataResult<out R> {
    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error(val code: Int? = null, val exception: Exception) : DataResult<Nothing>()
}

fun <T> DataResult<T>.successOr(fallback: T): T {
    return (this as? DataResult.Success<T>)?.data ?: fallback
}
