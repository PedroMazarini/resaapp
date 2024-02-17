package com.resa.data.network.datasource

import com.resa.data.network.datasource.abstraction.StopPointsDatasource
import com.resa.data.network.mappers.DomainStopPoints
import com.resa.data.network.mappers.RemoteToDomainStopPointsMapper
import com.resa.data.network.model.geography.stopareas.response.StopPointSimplified
import com.resa.data.network.services.RetrofitService
import com.resa.data.network.services.geography.StopPointsService
import com.resa.data.network.services.travelplanner.StopDeparturesService
import com.resa.data.network.util.Polygon
import com.resa.domain.model.Coordinate
import com.resa.global.loge
import javax.inject.Inject
import com.resa.domain.model.stoparea.StopPoint as DomainStopPoint

class StopPointsDatasourceImpl
@Inject
constructor(
    private val remoteToDomainStopPointsMapper: RemoteToDomainStopPointsMapper,
) : StopPointsDatasource {

    private val stopPointsService = RetrofitService.getInstance(
        StopPointsService::class.java,
    )
    private val stopDeparturesService =
        RetrofitService.getInstance(
            StopDeparturesService::class.java,
        )

    @Throws
    override suspend fun getDeparturesAround(
        coordinate: Coordinate,
        token: String
    ): Result<DomainStopPoints> = runCatching {
        val stopPointsResult = getStopPoints(coordinate, token)
        val stopPoints: MutableSet<DomainStopPoint> = mutableSetOf()

        if (stopPointsResult.isNotEmpty()) {
            stopPointsResult.forEach {
                it.gid?.let { gid ->
                    val response = stopDeparturesService.getStopDepartures(
                        auth = "Bearer $token",
                        stopPointGid = gid,
                    )
                    response.results?.let { results ->
                            stopPoints.addAll(remoteToDomainStopPointsMapper.map(results))
                    }
                }?: run {
                    return Result.failure(NullPointerException("gid is null"))
                }
            }
            return Result.success(stopPoints.toList())
        } else {
            return Result.failure(Exception("stopPointsResult is empty"))
        }
    }

    private suspend fun getStopPoints(
        coordinate: Coordinate,
        token: String
    ): List<StopPointSimplified> {
        val params = mapOf(
            "spatialFilter" to Polygon.createSquarePolygon(coordinate),
        )
        return stopPointsService.getStopsAround(
            auth = "Bearer $token",
            paramsMap = params,
        ).stopPoints.orEmpty()
    }
}
