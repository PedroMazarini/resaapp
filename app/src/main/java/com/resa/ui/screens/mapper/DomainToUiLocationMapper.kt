package com.resa.ui.screens.mapper

import com.resa.global.Mapper
import com.resa.ui.model.LocationType
import com.resa.domain.model.Location as DomainLocation
import com.resa.ui.model.Location as UiLocation

class DomainToUiLocationMapper : Mapper<DomainLocation, UiLocation> {
    override fun map(value: DomainLocation): UiLocation =
        UiLocation(
            id = value.id,
            name = value.name,
            lat = value.lat,
            lon = value.lon,
            type = LocationType fromString value.type.name,
        )
}
