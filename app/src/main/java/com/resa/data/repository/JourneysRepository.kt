package com.resa.data.repository

import android.content.Context
import androidx.annotation.WorkerThread
import com.resa.data.network.datasource.abstraction.JourneysDatasource
import com.resa.data.network.model.journeys.QueryJourneysParams
import com.resa.global.preferences.PrefsProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JourneysRepository
@Inject
constructor(
    private val journeysDatasource: JourneysDatasource,
    private val prefsProvider: PrefsProvider,
) {

    @WorkerThread
    suspend fun queryJourneys(
        queryJourneysParams: QueryJourneysParams,
    ) = journeysDatasource.queryJourneys(
        journeysParams = queryJourneysParams,
        token = prefsProvider.getToken(),
    )
}