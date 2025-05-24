package com.mazarini.resa.ui.screens.departures.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazarini.resa.domain.usecases.location.GetRecentLocationsUseCase
import com.mazarini.resa.domain.usecases.location.SaveRecentLocationUseCase
import com.mazarini.resa.domain.usecases.stoparea.GetStopDeparturesUseCase
import com.mazarini.resa.domain.usecases.stoparea.GetStopsByCoordinateUseCase
import com.mazarini.resa.domain.usecases.stoparea.GetStopsByNameUseCase
import com.mazarini.resa.global.extensions.safeLet
import com.mazarini.resa.global.preferences.PrefsProvider
import com.mazarini.resa.global.preferences.model.PreferredStop
import com.mazarini.resa.ui.model.Location
import com.mazarini.resa.ui.model.LocationType
import com.mazarini.resa.ui.screens.locationsearch.state.CurrentLocation
import com.mazarini.resa.ui.screens.locationsearch.state.MIN_LENGTH_FOR_SEARCH
import com.mazarini.resa.ui.screens.mapper.DomainToUiLocationMapper
import com.mazarini.resa.ui.util.copyUpdate
import com.mazarini.resa.ui.util.distinctCollectLatest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mazarini.resa.domain.model.LocationType as DomainLocationType

@HiltViewModel
class DeparturesViewModel
@Inject
constructor(
    private val queryStopsByName: GetStopsByNameUseCase,
    private val queryStopByCoordinate: GetStopsByCoordinateUseCase,
    private val getStopDeparturesUseCase: GetStopDeparturesUseCase,
    private val getRecentLocationsUseCase: GetRecentLocationsUseCase,
    private val saveRecentLocationUseCase: SaveRecentLocationUseCase,
    private val locationMapper: DomainToUiLocationMapper,
    private val prefsProvider: PrefsProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeparturesUiState())
    val uiState: StateFlow<DeparturesUiState> = _uiState
    var isQueryingByLocation = false
    private var stopQueryJob: Job? = null

    init {
        observeStopQuery()
        observeSelectedStop()
        getRecentLocations()
        initPreferredStop()
    }

    fun onEvent(event: DeparturesUiEvent) {
        when (event) {
            is DeparturesUiEvent.OnQueryChanged -> stopQueryChanged(event.query)
            is DeparturesUiEvent.OnStopSelected -> onStopSelected(event.location)
            is DeparturesUiEvent.QueryByCoordinate -> startQueryJobByCoordinate(event)
            is DeparturesUiEvent.UserLocationRequest -> _uiState.copyUpdate {
                copy(userLocationRequest = event.currentLocation)
            }
            is DeparturesUiEvent.PinLocation -> {
                setPreferredStop(event.location)
            }
            DeparturesUiEvent.PullRefresh -> pullRefreshDepartures()
            DeparturesUiEvent.DeleteCurrentPreferred -> deleteCurrentPreferred()
            DeparturesUiEvent.StartLocationRequest -> startLocationRequest()
            DeparturesUiEvent.LocationRequestOngoing -> locationFailed()
        }
    }

    private fun pullRefreshDepartures() {
        _uiState.copyUpdate { copy(isLoading = true) }
        _uiState.value.selectedStop?.let { refreshDeparturesForLocation(it) }
    }

    private fun startLocationRequest() {
        _uiState.copyUpdate { copy(userLocationRequest = CurrentLocation.Request) }
    }

    private fun locationFailed() {
        _uiState.copyUpdate {
            copy(userLocationRequest = null, isLoading = false)
        }
    }

    private fun deleteCurrentPreferred() {
        viewModelScope.launch {
            _uiState.copyUpdate { copy(preferredStop = null) }
            prefsProvider.setPreferredDeparture(null)
        }
    }

    private fun getRecentLocations() {
        viewModelScope.launch {
            getRecentLocationsUseCase(
                types = listOf(DomainLocationType.stoparea),
                limit = MAX_RECENT_LOCATION,
            ).collectLatest {
                _uiState.copyUpdate {
                    copy(recentLocations = it.map(locationMapper::map))
                }
            }
        }
    }

    private fun saveRecentLocation(location: Location) {
        viewModelScope.launch {
            saveRecentLocationUseCase(locationMapper.reverse(location))
        }
    }

    private fun setPreferredStop(location: Location) {
        viewModelScope.launch {
            prefsProvider.setPreferredDeparture(
                PreferredStop(gid = location.id, name = location.name)
            )
            _uiState.copyUpdate { copy(selectedStop = location) }
            refreshPreferredStop()
        }
    }

    private fun initPreferredStop() {
        viewModelScope.launch {
            val preferredStop = prefsProvider.getPreferredDeparture()
            _uiState.copyUpdate { copy(preferredStop = preferredStop) }
            uiState.value.preferredStop?.let {
                _uiState.copyUpdate { copy(isLoading = true) }
                refreshDeparturesForLocation(
                    Location(
                        id = it.gid,
                        name = it.name,
                        type = LocationType.gps
                    )
                )
            } ?: run {
                _uiState.copyUpdate { copy(requestFocus = true) }
            }
        }
    }

    private fun refreshPreferredStop() {
        viewModelScope.launch {
            val preferredStop = prefsProvider.getPreferredDeparture()
            _uiState.copyUpdate { copy(preferredStop = preferredStop) }
        }
    }

    private fun observeSelectedStop() {
        viewModelScope.launch {
            combine(
                _uiState.map { it.selectedStop }.distinctUntilChanged(),
                _uiState.map { it.preferredStop }.distinctUntilChanged(),
            ) { selectedStop, preferredStop ->
                safeLet(selectedStop, preferredStop) { selected, preferred ->
                    selected.id == preferred.gid
                } ?: false
            }.collectLatest { isPreferredSelected ->
                _uiState.copyUpdate { copy(isPreferredSelected = isPreferredSelected) }
            }
        }
    }

    private fun onStopSelected(location: Location) {
        _uiState.copyUpdate {
            copy(selectedStop = location, isLoading = true)
        }
        saveRecentLocation(location)
        refreshDeparturesForLocation(location)
    }

    private fun refreshDeparturesForLocation(location: Location) {
        viewModelScope.launch {
            val departures = getStopDeparturesUseCase(location.id).sortedBy { it.platform }
            _uiState.copyUpdate {
                copy(departures = departures, isLoading = false, selectedStop = location)
            }
        }
    }

    private fun stopQueryChanged(query: String) {
        _uiState.copyUpdate {
            copy(selectedStop = null, stopQuery = query)
        }
    }

    private fun observeStopQuery() {
        viewModelScope.launch {
            _uiState.map { it.stopQuery }.distinctCollectLatest { query ->
                if (!isQueryingByLocation) {
                    _uiState.copyUpdate { copy(showStopQueryResult = false) }
                    if (query.length >= MIN_LENGTH_FOR_SEARCH) {
                        _uiState.copyUpdate { copy(showStopQueryResult = true) }
                        startQueryJobByName(query)
                    }
                }
            }
        }
    }

    private fun startQueryJobByName(name: String) {
        stopQueryJob?.cancel()
        _uiState.copyUpdate { copy(isLoading = true) }
        stopQueryJob = viewModelScope.launch {
            val queryResult = queryStopsByName(name).map { locationMapper.map(it) }
            _uiState.copyUpdate { copy(stopQueryResult = queryResult, isLoading = false) }
        }
    }

    private fun startQueryJobByCoordinate(event: DeparturesUiEvent.QueryByCoordinate) {
        stopQueryJob?.cancel()
        _uiState.copyUpdate {
            copy(isLoading = true, showStopQueryResult = true, stopQuery = event.locationName)
        }
        stopQueryJob = viewModelScope.launch {
            val queryResult = queryStopByCoordinate(event.coordinate).map {
                locationMapper.map(it)
            }
            _uiState.copyUpdate {
                copy(stopQueryResult = queryResult, isLoading = false)
            }
        }
        isQueryingByLocation = false
    }

    companion object {
        const val MAX_RECENT_LOCATION = 10
    }
}
