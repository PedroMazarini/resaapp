package com.mazarini.resa.data.network.datasource.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mazarini.resa.data.network.mappers.QueryLocationsParamsMapper
import com.mazarini.resa.data.network.model.travelplanner.location.QueryLocationsParams
import com.mazarini.resa.data.network.model.travelplanner.location.typesNames
import com.mazarini.resa.data.network.services.travelplanner.LocationsService
import com.mazarini.resa.data.network.services.travelplanner.LocationsService.Companion.PAGE_SIZE
import com.mazarini.resa.data.network.services.RetrofitService
import com.mazarini.resa.data.network.services.RetrofitService.BASE_URL_TRAVEL_PLANNER
import com.mazarini.resa.global.analytics.loge
import com.mazarini.resa.data.network.model.travelplanner.location.Location as RemoteLocation

class LocationsPagingSource(
    private val token: String,
    private val locationsParams: QueryLocationsParams.ByText,
    private val locationsService: LocationsService,
    private val mapper: QueryLocationsParamsMapper,
) : PagingSource<Int, RemoteLocation>() {

    override fun getRefreshKey(state: PagingState<Int, RemoteLocation>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RemoteLocation> {
        return try {
            val page = params.key ?: 1
            val response = locationsService.queryLocation(
                auth = "Bearer $token",
                queryMode = locationsParams.queryMode,
                queryMap = mapper.map(locationsParams.copyWithPage(page)),
                types = locationsParams.typesNames(),
            )

            LoadResult.Page(
                data = response.results ?: emptyList(),
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.results.isNullOrEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            loge("LocationsPagingSource", mapOf("LocationsPagingSource" to "failed with: ${e.message.orEmpty()}"))
            LoadResult.Error(e)
        }
    }
}

private fun QueryLocationsParams.ByText.copyWithPage(page: Int): QueryLocationsParams.ByText =
    this.copy(offset = (page - 1) * PAGE_SIZE)
