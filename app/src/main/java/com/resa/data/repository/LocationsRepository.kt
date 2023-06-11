package com.resa.data.repository

import androidx.annotation.WorkerThread
import com.resa.data.network.datasource.abstraction.LocationsDatasource
import com.resa.data.network.model.location.QueryLocationsParams
import com.resa.global.preferences.PrefsProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationsRepository
@Inject
constructor(
    private val locationsDatasource: LocationsDatasource,
    private val prefsProvider: PrefsProvider,
) {

    @WorkerThread
    suspend fun queryLocations(
        queryLocationsParams: QueryLocationsParams,
    ) = locationsDatasource.queryLocations(
        queryLocationsParams = queryLocationsParams,
        token = prefsProvider.getToken(),
    )
}
