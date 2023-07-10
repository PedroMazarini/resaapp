package com.resa.data.network.datasource.abstraction

import com.resa.domain.model.ApiToken

interface AuthenticationDatasource {

    suspend fun getToken(
        deviceId: String,
    ): ApiToken
}