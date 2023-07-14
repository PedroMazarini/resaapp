package com.resa.ui.screens.locationsearch.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.resa.domain.model.queryjourneys.QueryJourneysParams
import com.resa.domain.model.queryjourneys.QueryJourneysRelatesTo.ARRIVAL
import com.resa.domain.model.queryjourneys.QueryJourneysRelatesTo.DEPARTURE
import com.resa.domain.usecases.RefreshTokenUseCase
import com.resa.domain.usecases.journey.SaveCurrentJourneyQueryUseCase
import com.resa.domain.usecases.location.DeleteSavedLocationUseCase
import com.resa.domain.usecases.location.GetRecentLocationsUseCase
import com.resa.domain.usecases.location.GetSavedLocationsUseCase
import com.resa.domain.usecases.location.QueryLocationByTextUseCase
import com.resa.domain.usecases.location.SaveLocationUseCase
import com.resa.domain.usecases.location.SaveRecentLocationUseCase
import com.resa.global.extensions.isNotNull
import com.resa.global.extensions.isNull
import com.resa.global.extensions.rfc3339
import com.resa.global.extensions.setDateOnly
import com.resa.global.extensions.setHourMinute
import com.resa.global.logd
import com.resa.ui.model.Location
import com.resa.ui.model.LocationType
import com.resa.ui.screens.locationsearch.state.CurrentSearchType.DESTINATION
import com.resa.ui.screens.locationsearch.state.CurrentSearchType.ORIGIN
import com.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.ClearDest
import com.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.ClearOrigin
import com.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.DateFilterChanged
import com.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.DeleteFavorite
import com.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.DestFocusChanged
import com.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.DestSearchChanged
import com.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.IsDepartFilterChanged
import com.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.LocationSelected
import com.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.OriginFocusChanged
import com.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.OriginSearchChanged
import com.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.SwapLocations
import com.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.TimeFilterChanged
import com.resa.ui.screens.mapper.DomainToUiLocationMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class LocationSearchViewModel
@Inject
constructor(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val queryLocationByTextUseCase: QueryLocationByTextUseCase,
    private val saveCurrentJourneyQueryUseCase: SaveCurrentJourneyQueryUseCase,
    private val saveLocationUseCase: SaveLocationUseCase,
    private val deleteSavedLocationUseCase: DeleteSavedLocationUseCase,
    private val getSavedLocationsUseCase: GetSavedLocationsUseCase,
    private val saveRecentLocationUseCase: SaveRecentLocationUseCase,
    private val getRecentLocationsUseCase: GetRecentLocationsUseCase,
    private val locationMapper: DomainToUiLocationMapper,
) : ViewModel() {

    val uiState: LocationSearchUiState = LocationSearchUiState()
    private lateinit var originSearchJob: Job
    private lateinit var destSearchJob: Job
    private var queryJourneysParams = QueryJourneysParams()

    init {
        refreshToken()
        initSearchObservers()
        loadLocationsSuggestions()
    }

    private fun loadLocationsSuggestions() {
        viewModelScope.launch(Dispatchers.IO) {
            getSavedLocationsUseCase()
                .flowOn(Dispatchers.Main)
                .collectLatest {
                    uiState.savedLocations.value = locationMapper.map(it)
                    uiState.recentLocations.value =
                        uiState.recentLocations.value.filter { location ->
                            uiState.savedLocations.value.all { saved -> location.id != saved.id }
                        }
                }
        }
        viewModelScope.launch(Dispatchers.IO) {
            getRecentLocationsUseCase()
                .flowOn(Dispatchers.Main)
                .collectLatest {
                    uiState.recentLocations.value = locationMapper.map(it).filter { location ->
                        uiState.savedLocations.value.all { saved -> location.id != saved.id }
                    }
                }
        }
    }

    private fun initSearchObservers() {
        originSearchJob = viewModelScope.launch {
            uiState.originSearch.collectLatest {
                if (it.length >= MIN_LENGTH_FOR_SEARCH) queryLocations(it, ORIGIN)
            }
        }
        originSearchJob.start()
        destSearchJob = viewModelScope.launch {
            uiState.destSearch.collectLatest {
                if (it.length >= MIN_LENGTH_FOR_SEARCH) queryLocations(it, DESTINATION)
            }
        }
        destSearchJob.start()
    }

    private fun queryLocations(
        query: String,
        currentSearchType: CurrentSearchType,
    ) {
        viewModelScope.launch {
            val result = queryLocationByTextUseCase(query)
            when (currentSearchType) {
                ORIGIN -> {
                    uiState.originSearchRes.value = result.map { pagingData ->
                        pagingData.map {
                            locationMapper.map(it)
                        }
                    }
                }

                DESTINATION -> {
                    uiState.destSearchRes.value = result.map { pagingData ->
                        pagingData.map { locationMapper.map(it) }
                    }
                }
            }
        }
    }

    private fun refreshToken() =
        viewModelScope.launch {
            refreshTokenUseCase()
        }

    fun onEvent(event: LocationSearchUiEvent) {
        logd(className = TAG, "onEvent: $event")
        when (event) {
            ClearDest -> clearDestination()
            ClearOrigin -> clearOrigin()
            is DeleteFavorite -> {}
            SwapLocations -> swapLocations()
            is LocationSelected -> setSelectedLocation(event.location)
            is OriginSearchChanged -> updateOriginSearch(event.query)
            is DestSearchChanged -> updateDestSearch(event.query)
            is IsDepartFilterChanged -> setIsDepartureFilter(isDeparture = event.isDepart)
            is DateFilterChanged -> setDateFilter(event.date)
            is TimeFilterChanged -> setTimeFilter(event.date)
            LocationSearchUiEvent.NavigateToResults -> uiState.isSelectionComplete.value = false
            is OriginFocusChanged -> {
                uiState.requestOriginFocus.value = false
                setCurrentSearchType(isOrigin = event.hasFocus)
            }

            is DestFocusChanged -> {
                uiState.requestDestFocus.value = false
                setCurrentSearchType(isOrigin = event.hasFocus.not())
            }

            is LocationSearchUiEvent.LocationResult ->
                saveCurrentLocation(latitude = event.lat, longitude = event.lon)

            is LocationSearchUiEvent.RequestLocation ->
                uiState.currentLocationRequest.value = event.currentLocation

            is LocationSearchUiEvent.SaveLocation -> saveLocation(event.location)
            is LocationSearchUiEvent.DeleteLocation -> deleteLocation(event.id)
        }
    }

    private fun deleteLocation(id: String) {
        viewModelScope.launch(Dispatchers.IO) { deleteSavedLocationUseCase(id) }
    }

    private fun saveLocation(location: Location) {
        viewModelScope.launch(Dispatchers.IO) { saveLocationUseCase(locationMapper.reverse(location)) }
    }

    private fun updateOriginSearch(query: String) {
        uiState.originSelected.value = null
        uiState.originSearch.value = query
    }

    private fun updateDestSearch(query: String) {
        uiState.destSelected.value = null
        uiState.destSearch.value = query
    }

    private fun setDateFilter(newDate: Date) {
        uiState.journeyFilters.value = with(uiState.journeyFilters.value) {
            copy(
                date = this.date.setDateOnly(newDate),
            )
        }
    }

    private fun setTimeFilter(newTime: Date) {
        uiState.journeyFilters.value = with(uiState.journeyFilters.value) {
            copy(
                date = this.date.setHourMinute(newTime),
            )
        }
    }

    private fun setIsDepartureFilter(isDeparture: Boolean) {
        uiState.journeyFilters.value = uiState.journeyFilters.value.copy(
            isDepartureFilters = isDeparture
        )
    }

    private fun swapLocations() {
        uiState.originSelected.value = uiState.destSelected.value.also {
            uiState.destSelected.value = uiState.originSelected.value
        }
        uiState.originSearch.value = uiState.destSearch.value.also {
            uiState.destSearch.value = uiState.originSearch.value
        }
        if (uiState.originSelected.value != null)
            uiState.requestDestFocus.value = true
        else
            uiState.requestOriginFocus.value = true
    }

    private fun setCurrentSearchType(isOrigin: Boolean) {
        if (isOrigin)
            uiState.currentSearchType.value = ORIGIN
        else
            uiState.currentSearchType.value = DESTINATION
    }

    private fun clearOrigin() {
        uiState.originSearch.value = ""
        uiState.originSelected.value = null
        uiState.requestOriginFocus.value = true
    }

    private fun clearDestination() {
        uiState.destSearch.value = ""
        uiState.destSelected.value = null
        uiState.requestDestFocus.value = true
    }

    private fun setSelectedLocation(
        location: Location,
    ) {
        when (uiState.currentSearchType.value) {
            ORIGIN -> {
                uiState.originSelected.value = location
                if (uiState.destSelected.value.isNull)
                    uiState.requestDestFocus.value = true
            }

            DESTINATION -> {
                uiState.destSelected.value = location
                if (uiState.originSelected.value.isNull)
                    uiState.requestOriginFocus.value = true
            }
        }
        saveRecentLocation(location)
        updateQueryParams()
    }

    private fun saveRecentLocation(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            saveRecentLocationUseCase(locationMapper.reverse(location))
        }
    }

    private fun updateQueryParams() {
        uiState.originSelected.value?.let {
            queryJourneysParams = queryJourneysParams.copy(
                originGid = it.id,
                originName = it.name,
                originLatitude = it.lat,
                originLongitude = it.lon,
                originType = it.type.asJourneyParamsType(),
            )
        }
        uiState.destSelected.value?.let {
            queryJourneysParams = queryJourneysParams.copy(
                destinationGid = it.id,
                destinationName = it.name,
                destinationLatitude = it.lat,
                destinationLongitude = it.lon,
                destinationType = it.type.asJourneyParamsType(),
            )
        }
        uiState.journeyFilters.value.let {
            queryJourneysParams = queryJourneysParams.copy(
                dateTime = it.date.rfc3339(),
                dateTimeRelatesTo = if (it.isDepartureFilters) DEPARTURE else ARRIVAL,
            )
        }
        verifySelectionIsComplete()
    }

    private fun verifySelectionIsComplete() {
        if (uiState.originSelected.value.isNotNull && uiState.destSelected.value.isNotNull) {
            finishLocationSelection()
        }
    }

    private fun finishLocationSelection() {
        viewModelScope.launch {
            saveCurrentJourneyQueryUseCase(queryJourneysParams)
            uiState.isSelectionComplete.value = true
        }
    }

    private fun saveCurrentLocation(latitude: Double, longitude: Double) {
        val location = Location(
            id = "",
            name = "",
            lat = latitude,
            lon = longitude,
            type = LocationType.gps,
        )
        when (uiState.currentSearchType.value) {
            ORIGIN -> {
                if (uiState.destSelected.value.isNull) {
                    uiState.requestDestFocus.value = true
                }
                if (uiState.destSelected.value?.type == LocationType.gps) {
                    uiState.destSelected.value = null
                    uiState.requestDestFocus.value = true
                }
                uiState.originSelected.value = location
            }

            DESTINATION -> {
                if (uiState.originSelected.value.isNull) {
                    uiState.requestOriginFocus.value = true
                }
                if (uiState.originSelected.value?.type == LocationType.gps) {
                    uiState.originSelected.value = null
                    uiState.requestOriginFocus.value = true
                }
                uiState.destSelected.value = location
            }
        }
        updateQueryParams()
    }

    private companion object {
        const val TAG = "LocationSearchViewModel"
    }
}
