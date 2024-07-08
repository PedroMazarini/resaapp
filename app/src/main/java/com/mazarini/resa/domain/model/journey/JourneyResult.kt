package com.mazarini.resa.domain.model.journey

import androidx.paging.PagingData

sealed class JourneyResult {

    data class Upcoming(
        val journeyPages: PagingData<Journey>,
    ) : JourneyResult()

    data class Passed(
        val journeyPages: PagingData<Journey>,
    ) : JourneyResult()

}
