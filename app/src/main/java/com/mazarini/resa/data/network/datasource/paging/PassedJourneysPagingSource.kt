package com.mazarini.resa.data.network.datasource.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mazarini.resa.data.network.mappers.QueryJourneysParamsMapper
import com.mazarini.resa.data.network.model.travelplanner.journeys.GetJourneysResponse
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.Journey
import com.mazarini.resa.data.network.services.travelplanner.JourneysService
import com.mazarini.resa.data.network.services.RetrofitService
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysParams
import com.mazarini.resa.domain.model.queryjourneys.transportModesNames
import com.mazarini.resa.domain.model.queryjourneys.transportSubModesNames
import com.mazarini.resa.global.extensions.parseRfc3339
import com.mazarini.resa.global.analytics.loge
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.Journey as RemoteJourney

class PassedJourneysPagingSource(
    private val token: String,
    private val journeysParams: QueryJourneysParams,
) : PagingSource<Int, RemoteJourney>() {

    // TODO: Inject this field
    private val journeysService = RetrofitService.getInstance(
        JourneysService::class.java,
        baseUrl = RetrofitService.BASE_URL_TRAVEL_PLANNER,
    )

    // TODO: Inject this field
    private val mapper = QueryJourneysParamsMapper()

    private val passedUrlReferences = mutableMapOf<Int, String>()

    override fun getRefreshKey(state: PagingState<Int, RemoteJourney>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RemoteJourney> {
        return try {
            val page = params.key ?: 1

            val response = queryJourneys(token, page)
            val results = reorderResult(response.results)

            updatePageUrlReferences(page, response)
            LoadResult.Page(
                data = results ?: emptyList(),
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (results.isNullOrEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            loge("LocationsPagingSource failed with: ${e.message.orEmpty()}")
            LoadResult.Error(e)
        }
    }

    private fun reorderResult(results: List<Journey>?): List<Journey>? =
        results?.sortedByDescending {
            it.tripLegs?.first()?.plannedDepartureTime?.parseRfc3339()?.time
        }

    private suspend fun queryJourneys(
        token: String,
        page: Int,
    ): GetJourneysResponse {
        return if (passedUrlReferences.containsKey(page)) {
            loadFromCachedUrl(token, page)
        } else {
            val firstPage = loadInitialPage(token, page)

            firstPage.links?.previous?.let { prevUrl ->
                passedUrlReferences[page] = prevUrl
                loadFromCachedUrl(token, page)
            } ?: run {
                GetJourneysResponse()
            }
        }
    }

    private suspend fun loadInitialPage(token: String, page: Int): GetJourneysResponse {
        loge("loadInitialPage", mapOf("LocationsPagingSource" to "INITIAL reference: ${passedUrlReferences[page]}"))
        return journeysService.queryJourneys(
            auth = "Bearer $token",
            queryMap = mapper.map(journeysParams),
            transportModes = journeysParams.transportModesNames(),
            transportSubModes = journeysParams.transportSubModesNames(),
        )
    }

    private suspend fun loadFromCachedUrl(token: String, page: Int): GetJourneysResponse {
        loge("loadFromCachedUrl", mapOf("LocationsPagingSource" to "CACHED URL reference: ${passedUrlReferences[page]}"))
        return journeysService.queryJourneysByUrl(
            auth = "Bearer $token",
            params = passedUrlReferences[page].orEmpty(),
        )
    }

    private fun updatePageUrlReferences(page: Int, response: GetJourneysResponse) {
        response.links?.previous?.let { prevUrl ->
            passedUrlReferences[page.plus(1)] = prevUrl
        }
        response.links?.next?.let { nextUrl ->
            passedUrlReferences[page.minus(1)] = nextUrl
        }
        response.links?.current?.let { currentUrl ->
            passedUrlReferences[page] = currentUrl
        }
    }
}