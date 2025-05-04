package com.mazarini.resa.data.network.datasource

import com.mazarini.resa.data.network.datasource.abstraction.AuthenticationDatasource
import com.mazarini.resa.data.network.services.AuthenticationService
import com.mazarini.resa.data.network.services.RetrofitService
import com.mazarini.resa.domain.model.ApiToken
import okhttp3.internal.toLongOrDefault
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
            expireMilli = tokenResponse.expireInSec.toLongOrDefault(0L),
        )
    }
}
