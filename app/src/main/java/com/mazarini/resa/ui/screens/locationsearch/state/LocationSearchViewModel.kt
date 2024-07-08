package com.mazarini.resa.ui.screens.locationsearch.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.mazarini.resa.domain.model.JourneySearch
import com.mazarini.resa.domain.model.TransportMode
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysParams
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysRelatesTo.ARRIVAL
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysRelatesTo.DEPARTURE
import com.mazarini.resa.domain.usecases.RefreshTokenUseCase
import com.mazarini.resa.domain.usecases.journey.GetCurrentJourneyQueryUseCase
import com.mazarini.resa.domain.usecases.journey.SaveCurrentJourneyQueryUseCase
import com.mazarini.resa.domain.usecases.journey.SaveRecentJourneySearchUseCase
import com.mazarini.resa.domain.usecases.location.DeleteSavedLocationUseCase
import com.mazarini.resa.domain.usecases.location.GetRecentLocationsUseCase
import com.mazarini.resa.domain.usecases.location.GetSavedLocationsUseCase
import com.mazarini.resa.domain.usecases.location.QueryLocationByTextUseCase
import com.mazarini.resa.domain.usecases.location.SaveLocationUseCase
import com.mazarini.resa.domain.usecases.location.SaveRecentLocationUseCase
import com.mazarini.resa.global.extensions.isNull
import com.mazarini.resa.global.extensions.parseRfc3339
import com.mazarini.resa.global.extensions.rfc3339
import com.mazarini.resa.global.extensions.safeLet
import com.mazarini.resa.global.extensions.setDateOnly
import com.mazarini.resa.global.extensions.setHourMinute
import com.mazarini.resa.global.analytics.logd
import com.mazarini.resa.global.preferences.PrefsProvider
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state.JourneyFilterUiEvent
import com.mazarini.resa.ui.model.Location
import com.mazarini.resa.ui.model.LocationType
import com.mazarini.resa.ui.screens.locationsearch.model.JourneyFilters
import com.mazarini.resa.ui.screens.locationsearch.state.CurrentSearchType.DESTINATION
import com.mazarini.resa.ui.screens.locationsearch.state.CurrentSearchType.ORIGIN
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.ClearDest
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.ClearOrigin
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.DeleteFavorite
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.DestFocusChanged
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.DestSearchChanged
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.LocationSelected
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.OriginFocusChanged
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.OriginSearchChanged
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.SwapLocations
import com.mazarini.resa.ui.screens.mapper.DomainToUiLocationMapper
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
    private val saveRecentJourneySearchesUseCase: SaveRecentJourneySearchUseCase,
    private val deleteSavedLocationUseCase: DeleteSavedLocationUseCase,
    private val getSavedLocationsUseCase: GetSavedLocationsUseCase,
    private val saveRecentLocationUseCase: SaveRecentLocationUseCase,
    private val getRecentLocationsUseCase: GetRecentLocationsUseCase,
    private val getCurrentJourneyQueryUseCase: GetCurrentJourneyQueryUseCase,
    private val locationMapper: DomainToUiLocationMapper,
    private val prefsProvider: PrefsProvider,
) : ViewModel() {

    val uiState: LocationSearchUiState = LocationSearchUiState()
    private lateinit var originSearchJob: Job
    private lateinit var destSearchJob: Job
    private var queryJourneysParams = QueryJourneysParams()

    init {
        refreshToken()
        initSearchObservers()
        loadLocationsSuggestions()
        loadCurrentPreferredModes()
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

    private fun loadCurrentPreferredModes() {
        viewModelScope.launch {
            prefsProvider.collectPreferredTransportModes().collectLatest {
                uiState.filtersUiState.preferredModes.value = it
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
                saveCurrentLocation(name = event.name, latitude = event.lat, longitude = event.lon)

            is LocationSearchUiEvent.RequestLocation ->
                uiState.currentLocationRequest.value = event.currentLocation

            is LocationSearchUiEvent.SaveLocation -> saveLocation(event.location)
            is LocationSearchUiEvent.DeleteLocation -> deleteLocation(event.id)
            is LocationSearchUiEvent.FilterEvent -> interceptFilterEvent(event.filterUiEvent)
        }
    }

    private fun interceptFilterEvent(filterUiEvent: JourneyFilterUiEvent) {
        when (filterUiEvent) {
            is JourneyFilterUiEvent.OnModesClicked -> {}
            is JourneyFilterUiEvent.OnDateChanged -> setDateFilter(filterUiEvent.date)
            is JourneyFilterUiEvent.OnReferenceChanged ->
                setIsDepartureFilter(isDeparture = filterUiEvent.isDepart)

            is JourneyFilterUiEvent.OnTimeChanged -> setTimeFilter(filterUiEvent.date)
            is JourneyFilterUiEvent.OnModesChanged -> {}
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
        uiState.filtersUiState.filters.value = with(uiState.filtersUiState.filters.value) {
            copy(
                date = this.date.setDateOnly(newDate),
            )
        }
    }

    private fun setTimeFilter(newTime: Date) {
        uiState.filtersUiState.filters.value = with(uiState.filtersUiState.filters.value) {
            copy(
                date = this.date.setHourMinute(newTime),
            )
        }
    }

    private fun setIsDepartureFilter(isDeparture: Boolean) {
        uiState.filtersUiState.filters.value = uiState.filtersUiState.filters.value.copy(
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
                transportModes = uiState.filtersUiState.preferredModes.value + TransportMode.walk,
            )
        }
        uiState.destSelected.value?.let {
            queryJourneysParams = queryJourneysParams.copy(
                destinationGid = it.id,
                destinationName = it.name,
                destinationLatitude = it.lat,
                destinationLongitude = it.lon,
                destinationType = it.type.asJourneyParamsType(),
                transportModes = uiState.filtersUiState.preferredModes.value + TransportMode.walk,
            )
        }
        uiState.filtersUiState.filters.value.let {
            queryJourneysParams = queryJourneysParams.copy(
                dateTime = it.date.rfc3339(),
                dateTimeRelatesTo = if (it.isDepartureFilters) DEPARTURE else ARRIVAL,
            )
        }
        verifySelectionIsComplete()
    }

    private fun verifySelectionIsComplete() {
        safeLet(uiState.originSelected.value, uiState.destSelected.value) { origin, dest ->
            saveRecentJourneySearch(origin, dest)
            finishLocationSelection()
        }
    }

    private fun saveRecentJourneySearch(origin: Location, dest: Location) {
        viewModelScope.launch {
            saveRecentJourneySearchesUseCase(
                JourneySearch(
                    id = origin.customId() + dest.customId(),
                    origin = locationMapper.reverse(origin),
                    destination = locationMapper.reverse(dest),
                )
            )
        }
    }

    private fun finishLocationSelection() {
        viewModelScope.launch {
            saveCurrentJourneyQueryUseCase(queryJourneysParams)
            uiState.isSelectionComplete.value = true
        }
    }

    private fun saveCurrentLocation(latitude: Double, longitude: Double, name: String) {
        val location = Location(
            id = "",
            name = name,
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

    private fun updateFilters(queryParams: QueryJourneysParams) {
        uiState.filtersUiState.filters.value = JourneyFilters(
            date = queryParams.dateTime?.parseRfc3339() ?: Date(),
            isDepartureFilters = queryParams.dateTimeRelatesTo == DEPARTURE,
        )
    }

    fun verifyQueryFilters() {
        viewModelScope.launch {
            getCurrentJourneyQueryUseCase()
                .onSuccess { queryParams ->
                    if (queryParams.id == queryJourneysParams.id)
                        updateFilters(queryParams)
                }
        }
    }

    private companion object {
        const val TAG = "LocationSearchViewModel"
    }
}
