package com.mazarini.resa.data.network.datasource

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mazarini.resa.data.network.datasource.abstraction.JourneysDatasource
import com.mazarini.resa.data.network.datasource.paging.JourneysPagingSource
import com.mazarini.resa.data.network.datasource.paging.PassedJourneysPagingSource
import com.mazarini.resa.data.network.mappers.RemoteToDomainJourneyMapper
import com.mazarini.resa.data.network.mappers.RemoteToDomainLegDetailsMapper
import com.mazarini.resa.data.network.mappers.RemoteToDomainVehicleMapper
import com.mazarini.resa.data.network.services.travelplanner.JourneysService
import com.mazarini.resa.data.network.services.travelplanner.JourneysService.Companion.PAGE_SIZE
import com.mazarini.resa.domain.model.VehiclePosition
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.mazarini.resa.domain.model.journey.Journey as DomainJourney
import com.mazarini.resa.domain.model.journey.LegDetails as DomainLegTransportDetails

class JourneysDatasourceImpl
@Inject
constructor(
    private val remoteToDomainJourneyMapper: RemoteToDomainJourneyMapper,
    private val remoteToDomainLegDetailsMapper: RemoteToDomainLegDetailsMapper,
    private val remoteToDomainVehicleMapper: RemoteToDomainVehicleMapper,
    private val journeysService: JourneysService,
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

    override suspend fun getJourneyDetails(detailsRef: String, token: String): Result<List<DomainLegTransportDetails>> {
        val response = journeysService.getJourneyDetails(
            auth = "Bearer $token",
            detailsRef = detailsRef,
            includes = listOf("servicejourneycoordinates", "servicejourneycalls", "links"),
        )
        return response.let {
            Result.success(remoteToDomainLegDetailsMapper.map(it))
        }
    }

    override suspend fun getJourneyVehicles(detailsRef: String, token: String): Result<List<VehiclePosition>> {
        val params = mapOf(
            "detailsReferences" to detailsRef,
            "lowerLeftLat" to "57.080929",
            "lowerLeftLong" to "10.9210113",
            "upperRightLat" to "59.279379",
            "upperRightLong" to "14.932785",
        )
        val response = journeysService.getJourneyVehicles(
            auth = "Bearer $token",
            queryMap = params,
        )
        return Result.success(remoteToDomainVehicleMapper.map(response))
    }
}
