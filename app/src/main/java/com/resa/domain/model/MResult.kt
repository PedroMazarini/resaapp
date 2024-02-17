package com.resa.domain.model

sealed class MResult<out T> {
    data class Success<out T>(val data: T) : MResult<T>()
    object Loading : MResult<Nothing>()
    data class Failed(val error: Throwable) : MResult<Nothing>()
}
