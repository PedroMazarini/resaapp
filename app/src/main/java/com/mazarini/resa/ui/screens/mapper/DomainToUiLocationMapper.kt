package com.mazarini.resa.ui.screens.mapper

import com.mazarini.resa.domain.model.LocationType
import com.mazarini.resa.global.Mapper
import com.mazarini.resa.domain.model.Location as DomainLocation
import com.mazarini.resa.ui.model.Location as UiLocation

class DomainToUiLocationMapper : Mapper<DomainLocation, UiLocation> {
    override fun map(value: DomainLocation): UiLocation =
        UiLocation(
            id = value.id,
            name = value.name,
            lat = value.lat,
            lon = value.lon,
            type = com.mazarini.resa.ui.model.LocationType fromString value.type.name,
        )

    override fun reverse(value: UiLocation): DomainLocation =
        DomainLocation(
            id = value.id,
            name = value.name,
            lat = value.lat,
            lon = value.lon,
            type = LocationType fromString value.type.name,
        )
}
