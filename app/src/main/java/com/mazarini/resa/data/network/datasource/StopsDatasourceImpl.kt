package com.mazarini.resa.data.network.datasource

import com.mazarini.resa.data.network.datasource.abstraction.StopsDatasource
import com.mazarini.resa.data.network.mappers.QueryLocationsParamsMapper
import com.mazarini.resa.data.network.mappers.RemoteToDomainLocationMapper
import com.mazarini.resa.data.network.mappers.RemoteToDomainStopJourneyMapper
import com.mazarini.resa.data.network.model.travelplanner.location.QueryLocationType
import com.mazarini.resa.data.network.model.travelplanner.location.QueryLocationsParams
import com.mazarini.resa.data.network.model.travelplanner.location.typesNames
import com.mazarini.resa.data.network.services.travelplanner.LocationsService
import com.mazarini.resa.data.network.services.travelplanner.StopDeparturesService
import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.model.stoparea.StopJourney
import com.mazarini.resa.domain.repositoryAbstraction.DomainStopAreas
import javax.inject.Inject

class StopsDatasourceImpl
@Inject
constructor(
    private val paramsMapper: QueryLocationsParamsMapper,
    private val locationMapper: RemoteToDomainLocationMapper,
    private val stopJourneyMapper: RemoteToDomainStopJourneyMapper,
    private val locationsService: LocationsService,
    private val stopDeparturesService: StopDeparturesService,
) : StopsDatasource {

    override suspend fun getStopsByCoordinate(
        coordinate: Coordinate,
        token: String,
    ): DomainStopAreas {
        val params = QueryLocationsParams.ByCoordinates(
            latitude = coordinate.lat,
            longitude = coordinate.lon,
            limit = 30,
            types = listOf(QueryLocationType.stoparea),
        )
        return locationsService.queryLocation(
            auth = "Bearer $token",
            queryMode = params.queryMode,
            queryMap = paramsMapper.map(params),
            types = params.typesNames(),
        ).results?.map(locationMapper::map) ?: emptyList()
    }

    override suspend fun getStopsByName(
        query: String,
        token: String
    ): DomainStopAreas {
        val params = QueryLocationsParams.ByText(
            query = query,
            limit = 30,
            types = listOf(QueryLocationType.stoparea),
        )
        return locationsService.queryLocation(
            auth = "Bearer $token",
            queryMode = params.queryMode,
            queryMap = paramsMapper.map(params),
            types = params.typesNames(),
        ).results?.map(locationMapper::map) ?: emptyList()
    }

    override suspend fun getStopDepartures(
        stopAreaGid: String,
        token: String
    ): List<StopJourney> {
        val params = mapOf(
            "limit" to "30",
            "maxDeparturesPerLineAndDirection" to "2",
        )
        return stopDeparturesService.getStopDepartures(
            auth = "Bearer $token",
            stopAreaGid = stopAreaGid,
            queryMap = params,
        ).results?.map(stopJourneyMapper::map) ?: emptyList()
    }
}
