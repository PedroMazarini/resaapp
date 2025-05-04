package com.mazarini.resa.data.network.datasource.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mazarini.resa.data.network.mappers.QueryJourneysParamsMapper
import com.mazarini.resa.data.network.model.travelplanner.journeys.GetJourneysResponse
import com.mazarini.resa.data.network.services.travelplanner.JourneysService
import com.mazarini.resa.data.network.services.travelplanner.JourneysService.Companion.PAGE_SIZE
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysParams
import com.mazarini.resa.domain.model.queryjourneys.transportModesNames
import com.mazarini.resa.domain.model.queryjourneys.transportSubModesNames
import com.mazarini.resa.global.analytics.loge
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.Journey as RemoteJourney

class JourneysPagingSource(
    private val token: String,
    private val journeysParams: QueryJourneysParams,
    private val journeysService: JourneysService,
    private val mapper: QueryJourneysParamsMapper,
) : PagingSource<Int, RemoteJourney>() {

    private val pageUrlReferences = mutableMapOf<Int, String>()

    override fun getRefreshKey(state: PagingState<Int, RemoteJourney>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RemoteJourney> {
        return try {
            val page = params.key ?: 1

            val response = queryJourneys(token, journeysParams, page)

            updatePageUrlReferences(page, response)
            LoadResult.Page(
                data = response.results ?: emptyList(),
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.results.isNullOrEmpty()) null else page.plus(1),
//                nextKey = response.validate(page),
            )
        } catch (e: Exception) {
            loge("LocationsPagingSource failed with: ${e.message.orEmpty()}")
            LoadResult.Error(e)
        }
    }

    private suspend fun queryJourneys(
        token: String,
        journeysParams: QueryJourneysParams,
        page: Int,
    ): GetJourneysResponse {
        return if (pageUrlReferences.containsKey(page)) {
            journeysService.queryJourneysByUrl(
                auth = "Bearer $token",
                params = pageUrlReferences[page].orEmpty(),
            )
        } else {
            journeysService.queryJourneys(
                auth = "Bearer $token",
                queryMap = mapper.map(journeysParams),
                transportModes = journeysParams.transportModesNames(),
                transportSubModes = journeysParams.transportSubModesNames(),
            )
        }
    }

    private fun updatePageUrlReferences(page: Int, response: GetJourneysResponse) {
        response.links?.previous?.let { prevUrl ->
            pageUrlReferences[page.minus(1)] = prevUrl
        }
        response.links?.next?.let { nextUrl ->
            pageUrlReferences[page.plus(1)] = nextUrl
        }
        response.links?.current?.let { currentUrl ->
            pageUrlReferences[page] = currentUrl
        }
    }

    private fun GetJourneysResponse.validate(page: Int): Int? =
        if (this.results.isNullOrEmpty() || this.results.size < PAGE_SIZE) null
        else page.plus(1)

}