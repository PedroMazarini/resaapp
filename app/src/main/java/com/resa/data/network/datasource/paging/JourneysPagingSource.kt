package com.resa.data.network.datasource.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.resa.data.network.mappers.QueryJourneysParamsMapper
import com.resa.data.network.model.journeys.GetJourneysResponse
import com.resa.data.network.model.journeys.response.Journey as RemoteJourney
import com.resa.data.network.services.JourneysService
import com.resa.data.network.services.RetrofitService
import com.resa.domain.model.queryjourneys.QueryJourneysParams
import com.resa.domain.model.queryjourneys.transportModesNames
import com.resa.domain.model.queryjourneys.transportSubModesNames
import com.resa.global.loge

class JourneysPagingSource(
    private val token: String,
    private val journeysParams: QueryJourneysParams,
) : PagingSource<Int, RemoteJourney>() {

    // TODO: Inject this field
    private val journeysService = RetrofitService.getInstance(
        JourneysService::class.java,
        baseUrl = RetrofitService.BASE_URL_QUERIES,
    )
    // TODO: Inject this field
    private val mapper = QueryJourneysParamsMapper()

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
            )
        } catch (e: Exception) {
            loge("LocationsPagingSource failed with: ${e.message.orEmpty()}")
            LoadResult.Error(e)
        }
    }

    private suspend fun queryJourneys(
        token: String,
        journeysParams: QueryJourneysParams,
        page: Int
    ): GetJourneysResponse {
        return if (pageUrlReferences.containsKey(page)) {
            loge("LocationsPagingSource requesting from cached url reference: ${pageUrlReferences[page]}")
            journeysService.queryJourneysByUrl(
                auth = "Bearer $token",
                params = pageUrlReferences[page].orEmpty(),
            )
        } else {
            loge("LocationsPagingSource requesting params and filters")
            journeysService.queryJourneys(
                auth = "Bearer $token",
                queryMap = mapper.map(journeysParams),
                transportModes = journeysParams.transportModesNames(),
                transportSubModes = journeysParams.transportSubModesNames(),
            )
        }
    }

    private fun updatePageUrlReferences(page: Int, response: GetJourneysResponse) {
        response.links?.next?.let { nextUrl ->
            pageUrlReferences[page.plus(1)] = nextUrl
        }
        response.links?.current?.let { currentUrl ->
            pageUrlReferences[page] = currentUrl
        }
    }
}