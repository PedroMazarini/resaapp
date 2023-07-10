package com.resa.data.network.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.resa.data.network.datasource.abstraction.LocationsDatasource
import com.resa.data.network.datasource.paging.LocationsPagingSource
import com.resa.data.network.mappers.RemoteToDomainLocationMapper
import com.resa.data.network.model.location.QueryLocationsParams
import com.resa.data.network.services.LocationsService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.resa.domain.model.Location as DomainLocation

class LocationsDatasourceImpl
@Inject
constructor(
    private val remoteToDomainLocationMapper: RemoteToDomainLocationMapper,
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
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { remoteLocation ->
                remoteToDomainLocationMapper.map(remoteLocation)
            }
        }
    }
}
