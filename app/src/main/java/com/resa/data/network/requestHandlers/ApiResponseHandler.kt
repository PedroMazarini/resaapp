package com.resa.data.network.requestHandlers

import com.resa.data.network.requestHandlers.NetworkErrors.NETWORK_DATA_NULL
import com.resa.data.network.requestHandlers.NetworkErrors.NETWORK_ERROR

abstract class ApiResponseHandler<Data>(
    private val response: ApiResult<Data?>,
) {
    suspend fun getResult(): DataResult<Data> {
        return when (response) {
            is ApiResult.GenericError -> getErrorResponse(response.code, response.errorMessage)
            is ApiResult.NetworkError -> getErrorResponse(reason = NETWORK_ERROR)
            is ApiResult.Success -> {
                if (response.value == null) getErrorResponse(reason = NETWORK_DATA_NULL)
                else handleSuccess(response.value)
            }
        }
    }

    abstract suspend fun handleSuccess(resultData: Data): DataResult<Data>

    private fun getErrorResponse(code: Int? = null, reason: String?) =
        DataResult.Error(code = code, exception = Exception(reason))
}