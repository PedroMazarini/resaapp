package com.resa.data.repository

import androidx.annotation.WorkerThread
import com.resa.data.network.datasource.abstraction.LocationsDatasource
import com.resa.data.network.model.location.QueryLocationsParams
import com.resa.domain.repositoryAbstraction.LocationsRepository
import com.resa.global.preferences.PrefsProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationsRepositoryImpl
@Inject
constructor(
    private val locationsDatasource: LocationsDatasource,
    private val prefsProvider: PrefsProvider,
) : LocationsRepository {

    @WorkerThread
    override suspend fun queryLocationByText(
        query: String,
    ) = locationsDatasource.queryLocationsByText(
        queryLocationsParams = QueryLocationsParams.ByText(
            query = query,
        ),
        token = prefsProvider.getToken(),
    ).flowOn(Dispatchers.IO)
}
