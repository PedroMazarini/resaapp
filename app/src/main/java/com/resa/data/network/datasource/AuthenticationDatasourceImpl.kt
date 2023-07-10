package com.resa.data.network.datasource

import com.resa.data.network.datasource.abstraction.AuthenticationDatasource
import com.resa.data.network.services.AuthenticationService
import com.resa.data.network.services.RetrofitService
import com.resa.domain.model.ApiToken
import com.resa.global.extensions.toIntOrZero
import javax.inject.Inject

class AuthenticationDatasourceImpl
@Inject
constructor(
    private val retrofitService: RetrofitService,
) : AuthenticationDatasource {

    override suspend fun getToken(deviceId: String): ApiToken {
        val tokenResponse = retrofitService.getInstance(AuthenticationService::class.java)
            .requestToken(clientId = deviceId)
        return ApiToken(
            token = tokenResponse.token,
            expireInSec = tokenResponse.expireInSec.toIntOrZero,
        )
    }
}
