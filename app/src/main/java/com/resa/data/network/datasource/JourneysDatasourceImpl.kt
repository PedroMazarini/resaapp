package com.resa.data.network.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.resa.data.network.datasource.abstraction.JourneysDatasource
import com.resa.data.network.datasource.paging.JourneysPagingSource
import com.resa.data.network.datasource.paging.PassedJourneysPagingSource
import com.resa.data.network.mappers.RemoteToDomainJourneyMapper
import com.resa.data.network.services.travelplanner.JourneysService.Companion.PAGE_SIZE
import com.resa.domain.model.queryjourneys.QueryJourneysParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.resa.domain.model.journey.Journey as DomainJourney

class JourneysDatasourceImpl
@Inject
constructor(
    private val remoteToDomainJourneyMapper: RemoteToDomainJourneyMapper,
) : JourneysDatasource {

    override suspend fun queryJourneys(
        journeysParams: QueryJourneysParams,
        token: String,
    ): Flow<PagingData<DomainJourney>> {

        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                JourneysPagingSource(
                    token = token,
                    journeysParams = journeysParams,
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { remoteLocation ->
                remoteToDomainJourneyMapper.map(remoteLocation)
            }
        }
    }

    override suspend fun queryPassedJourneys(
        journeysParams: QueryJourneysParams,
        token: String,
    ): Flow<PagingData<DomainJourney>> {

        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                PassedJourneysPagingSource(
                    token = token,
                    journeysParams = journeysParams,
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { remoteLocation ->
                remoteToDomainJourneyMapper.map(remoteLocation)
            }
        }
    }
}
