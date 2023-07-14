package com.resa.ui.screens.mapper

import com.resa.global.Mapper
import javax.inject.Inject
import com.resa.domain.model.JourneySearch as DomainJourneySearch
import com.resa.ui.model.JourneySearch as UiJourneySearch

class DomainToUiJourneySearchMapper
@Inject
constructor(
    private val locationMapper: DomainToUiLocationMapper,
) : Mapper<DomainJourneySearch, UiJourneySearch> {

    override fun map(value: DomainJourneySearch): UiJourneySearch =
        UiJourneySearch(
            id = value.id,
            origin = locationMapper.map(value.origin),
            destination = locationMapper.map(value.destination),
        )

    override fun reverse(value: UiJourneySearch): DomainJourneySearch =
        DomainJourneySearch(
            id = value.id,
            origin = locationMapper.reverse(value.origin),
            destination = locationMapper.reverse(value.destination),
        )
}
