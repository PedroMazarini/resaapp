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
import com.mazarini.resa.global.analytics.logd
import com.mazarini.resa.global.extensions.isNull
import com.mazarini.resa.global.extensions.parseRfc3339
import com.mazarini.resa.global.extensions.rfc3339
import com.mazarini.resa.global.extensions.safeLet
import com.mazarini.resa.global.extensions.setDateOnly
import com.mazarini.resa.global.extensions.setHourMinute
import com.mazarini.resa.global.preferences.PrefsProvider
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state.JourneyFilterUiEvent
import com.mazarini.resa.ui.model.Location
import com.mazarini.resa.ui.model.LocationType
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
import com.mazarini.resa.ui.util.distinctCollectLatest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
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

    private val _uiState = MutableStateFlow(LocationSearchUiState())
    val uiState: StateFlow<LocationSearchUiState> = _uiState

    private var queryJourneysParams = QueryJourneysParams()

    init {
        refreshToken()
        initSearchObservers()
        loadLocationsSuggestions()
        loadCurrentPreferredModes()
    }

    private fun loadLocationsSuggestions() {
        viewModelScope.launch(Dispatchers.IO) {
            getSavedLocationsUseCase().collectLatest { savedLocations ->
                _uiState.update {
                    val savedIds = savedLocations.map { saved -> saved.id }.toSet()
                    it.copy(
                        savedLocations = locationMapper.map(savedLocations),
                        recentLocations = it.recentLocations.filter { location ->
                            location.id !in savedIds
                        }
                    )
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            getRecentLocationsUseCase().collectLatest { recentLocations ->
                _uiState.update { state ->
                    state.copy(
                        recentLocations = locationMapper.map(recentLocations).filter { location ->
                            location.id !in state.savedLocations.map { saved -> saved.id }
                        }
                    )
                }
            }
        }
    }

    private fun loadCurrentPreferredModes() {
        viewModelScope.launch {
            prefsProvider.collectPreferredTransportModes().collectLatest { modes ->
                _uiState.update { state ->
                    state.copy(
                        filtersUiState = state.filtersUiState.copy(
                            preferredModes = modes
                        )
                    )
                }
            }
        }
    }

    private fun initSearchObservers() {
        viewModelScope.launch {
            _uiState.map { it.originSearch }.distinctCollectLatest { originSearch ->
                if (originSearch.length >= MIN_LENGTH_FOR_SEARCH)
                    queryLocations(originSearch, ORIGIN)
            }
        }
        viewModelScope.launch {
            _uiState.map { it.destSearch }.distinctCollectLatest { destSearch ->
                if (destSearch.length >= MIN_LENGTH_FOR_SEARCH) queryLocations(
                    destSearch,
                    DESTINATION
                )
            }
        }
    }

    private fun queryLocations(
        query: String,
        currentSearchType: CurrentSearchType,
    ) {
        viewModelScope.launch {
            val result = queryLocationByTextUseCase(query)
            when (currentSearchType) {
                ORIGIN -> {
                    _uiState.update {
                        it.copy(
                            originSearchRes = result.map { pagingData ->
                                pagingData.map {
                                    locationMapper.map(it)
                                }
                            }
                        )
                    }
                }

                DESTINATION -> {
                    _uiState.update {
                        it.copy(
                            destSearchRes = result.map { pagingData ->
                                pagingData.map { locationMapper.map(it) }
                            }
                        )
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
            LocationSearchUiEvent.NavigateToResults -> _uiState.update { it.copy(isSelectionComplete = false) }
            is OriginFocusChanged -> {
                _uiState.update { it.copy(requestOriginFocus = false) }
                setCurrentSearchType(isOrigin = event.hasFocus)
            }

            is DestFocusChanged -> {
                _uiState.update { it.copy(requestDestFocus = false) }
                setCurrentSearchType(isOrigin = event.hasFocus.not())
            }

            is LocationSearchUiEvent.LocationResult ->
                saveCurrentLocation(name = event.name, latitude = event.lat, longitude = event.lon)

            is LocationSearchUiEvent.RequestLocation ->
                _uiState.update { it.copy(currentLocationRequest = event.currentLocation) }

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
            is JourneyFilterUiEvent.OnModesChanged -> updatePreferredModes(filterUiEvent.modes)
        }

    }

    private fun updatePreferredModes(modes: List<TransportMode>) {
        viewModelScope.launch {
            prefsProvider.setPreferredTransportModes(modes)
        }
    }

    private fun deleteLocation(id: String) {
        viewModelScope.launch(Dispatchers.IO) { deleteSavedLocationUseCase(id) }
    }

    private fun saveLocation(location: Location) {
        viewModelScope.launch(Dispatchers.IO) { saveLocationUseCase(locationMapper.reverse(location)) }
    }

    private fun updateOriginSearch(query: String) {
        _uiState.update {
            it.copy(
                originSelected = null,
                originSearch = query,
            )
        }
    }

    private fun updateDestSearch(query: String) {
        _uiState.update {
            it.copy(
                destSelected = null,
                destSearch = query,
            )
        }
    }

    private fun setDateFilter(newDate: Date) {
        _uiState.update { state ->
            state.copy(
                filtersUiState = state.filtersUiState.copy(
                    filters = state.filtersUiState.filters.copy(
                        date = state.filtersUiState.filters.date.setDateOnly(newDate)
                    )
                )
            )
        }
    }

    private fun setTimeFilter(newTime: Date) {
        _uiState.update { state ->
            state.copy(
                filtersUiState = state.filtersUiState.copy(
                    filters = state.filtersUiState.filters.copy(
                        date = state.filtersUiState.filters.date.setHourMinute(newTime)
                    )
                )
            )
        }
    }

    private fun setIsDepartureFilter(isDeparture: Boolean) {
        _uiState.update { state ->
            state.copy(
                filtersUiState = state.filtersUiState.copy(
                    filters = state.filtersUiState.filters.copy(
                        isDepartureFilters = isDeparture
                    )
                )
            )
        }
    }

    private fun swapLocations() {
        _uiState.update { state ->
            state.copy(
                originSelected = state.destSelected,
                destSelected = state.originSelected,
                requestDestFocus = state.originSelected.isNull,
                requestOriginFocus = state.destSelected.isNull,
            )
        }
    }

    private fun setCurrentSearchType(isOrigin: Boolean) {
        if (isOrigin)
            _uiState.update { it.copy(currentSearchType = ORIGIN) }
        else
            _uiState.update { it.copy(currentSearchType = DESTINATION) }
    }

    private fun clearOrigin() {
        _uiState.update {
            it.copy(
                originSearch = "",
                originSelected = null,
                requestOriginFocus = true,
            )
        }
    }

    private fun clearDestination() {
        _uiState.update {
            it.copy(
                destSearch = "",
                destSelected = null,
                requestDestFocus = true,
            )
        }
    }

    private fun setSelectedLocation(
        location: Location,
    ) {
        when (_uiState.value.currentSearchType) {
            ORIGIN -> {
                _uiState.update { it.copy(originSelected = location) }
                if (_uiState.value.destSelected.isNull)
                    _uiState.update { it.copy(requestDestFocus = true) }
            }

            DESTINATION -> {
                _uiState.update { it.copy(destSelected = location) }
                if (_uiState.value.originSelected.isNull)
                    _uiState.update { it.copy(requestOriginFocus = true) }
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
        _uiState.value.originSelected?.let {
            queryJourneysParams = queryJourneysParams.copy(
                originGid = it.id,
                originName = it.name,
                originLatitude = it.lat,
                originLongitude = it.lon,
                originType = it.type.asJourneyParamsType(),
                transportModes = _uiState.value.filtersUiState.preferredModes + TransportMode.walk,
            )
        }
        _uiState.value.destSelected?.let {
            queryJourneysParams = queryJourneysParams.copy(
                destinationGid = it.id,
                destinationName = it.name,
                destinationLatitude = it.lat,
                destinationLongitude = it.lon,
                destinationType = it.type.asJourneyParamsType(),
                transportModes = _uiState.value.filtersUiState.preferredModes + TransportMode.walk,
            )
        }
        _uiState.value.filtersUiState.filters.let {
            queryJourneysParams = queryJourneysParams.copy(
                dateTime = it.date.rfc3339(),
                dateTimeRelatesTo = if (it.isDepartureFilters) DEPARTURE else ARRIVAL,
            )
        }
        verifySelectionIsComplete()
    }

    private fun verifySelectionIsComplete() {
        safeLet(_uiState.value.originSelected, _uiState.value.destSelected) { origin, dest ->
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
            _uiState.update { it.copy(isSelectionComplete = true) }
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
        when (uiState.value.currentSearchType) {
            ORIGIN -> {
                if (_uiState.value.destSelected.isNull) {
                    _uiState.update { it.copy(requestDestFocus = true) }
                }
                if (_uiState.value.destSelected?.type == LocationType.gps) {
                    _uiState.update { it.copy(
                        destSelected = null,
                        requestDestFocus = true,
                    ) }
                }
                _uiState.update { it.copy(originSelected = location) }
            }

            DESTINATION -> {
                if (_uiState.value.originSelected.isNull) {
                    _uiState.update { it.copy(requestOriginFocus = true) }
                }
                if (uiState.value.originSelected?.type == LocationType.gps) {
                    _uiState.update { it.copy(
                        originSelected = null,
                        requestOriginFocus = true,
                    ) }
                }
                _uiState.update { it.copy(destSelected = location) }
            }
        }
        updateQueryParams()
    }

    private fun updateFilters(queryParams: QueryJourneysParams) {
        _uiState.update {
            it.copy(
                filtersUiState = it.filtersUiState.copy(
                    filters = it.filtersUiState.filters.copy(
                        date = queryParams.dateTime?.parseRfc3339() ?: Date(),
                        isDepartureFilters = queryParams.dateTimeRelatesTo == DEPARTURE,
                    )
                )
            )
        }
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
