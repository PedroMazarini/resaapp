package com.resa.data.network.requestHandlers

import com.resa.data.network.requestHandlers.NetworkErrors.NETWORK_ERROR_TIMEOUT
import com.resa.data.network.requestHandlers.NetworkErrors.NETWORK_ERROR_UNKNOWN
import com.resa.global.crashLog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

const val NETWORK_TIMEOUT = 6000L

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): ApiResult<T?> {
    return withContext(dispatcher) {
        try {
            withTimeout(NETWORK_TIMEOUT) {
                ApiResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    val code = 408 // timeout error code
                    ApiResult.GenericError(code, NETWORK_ERROR_TIMEOUT)
                }

                is IOException -> {
                    ApiResult.NetworkError
                }

                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    crashLog(errorResponse)
                    ApiResult.GenericError(code, errorResponse)
                }

                else -> {
                    crashLog(NETWORK_ERROR_UNKNOWN)
                    ApiResult.GenericError(
                        null,
                        NETWORK_ERROR_UNKNOWN,
                    )
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        NETWORK_ERROR_UNKNOWN
    }
}