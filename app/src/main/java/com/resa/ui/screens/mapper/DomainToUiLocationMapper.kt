package com.resa.ui.screens.mapper

import com.resa.global.Mapper
import com.resa.domain.model.Location as DomainLocation
import com.resa.domain.model.LocationType as DomainLocationType
import com.resa.ui.model.Location as UiLocation
import com.resa.ui.model.LocationType as UiLocationType

class DomainToUiLocationMapper : Mapper<DomainLocation, UiLocation> {
    override fun map(value: DomainLocation): UiLocation =
        UiLocation(
            id = value.id,
            name = value.name,
            lat = value.lat,
            lon = value.lon,
            type = UiLocationType fromString value.type.name,
        )

    override fun reverse(value: UiLocation): DomainLocation =
        DomainLocation(
            id = value.id,
            name = value.name,
            lat = value.lat,
            lon = value.lon,
            type = DomainLocationType fromString value.type.name,
        )
}
