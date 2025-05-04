package com.mazarini.resa.data.network.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mazarini.resa.data.network.datasource.abstraction.LocationsDatasource
import com.mazarini.resa.data.network.datasource.paging.LocationsPagingSource
import com.mazarini.resa.data.network.mappers.QueryLocationsParamsMapper
import com.mazarini.resa.data.network.mappers.RemoteToDomainLocationMapper
import com.mazarini.resa.data.network.model.travelplanner.location.QueryLocationsParams
import com.mazarini.resa.data.network.services.travelplanner.LocationsService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.mazarini.resa.domain.model.Location as DomainLocation

class LocationsDatasourceImpl
@Inject
constructor(
    private val remoteToDomainLocationMapper: RemoteToDomainLocationMapper,
    private val locationsService: LocationsService,
    private val mapper: QueryLocationsParamsMapper,
) : LocationsDatasource {

    override suspend fun queryLocationsByText(
        queryLocationsParams: QueryLocationsParams.ByText,
        token: String,
    ): Flow<PagingData<DomainLocation>> {

        return Pager(
            config = PagingConfig(pageSize = LocationsService.PAGE_SIZE),
            pagingSourceFactory = {
                LocationsPagingSource(
                    token = token,
                    locationsParams = queryLocationsParams,
                    locationsService = locationsService,
                    mapper = mapper,
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { remoteLocation ->
                remoteToDomainLocationMapper.map(remoteLocation)
            }
        }
    }
}
