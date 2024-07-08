package com.mazarini.resa.data.network.datasource.abstraction

import com.mazarini.resa.domain.model.ApiToken

interface AuthenticationDatasource {

    suspend fun getToken(
        deviceId: String,
    ): ApiToken
}