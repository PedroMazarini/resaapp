package com.mazarini.resa.ui.screens.departures.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.usecases.location.GetRecentLocationsUseCase
import com.mazarini.resa.domain.usecases.location.SaveRecentLocationUseCase
import com.mazarini.resa.domain.usecases.stoparea.GetStopDeparturesUseCase
import com.mazarini.resa.domain.usecases.stoparea.GetStopsByCoordinateUseCase
import com.mazarini.resa.domain.usecases.stoparea.GetStopsByNameUseCase
import com.mazarini.resa.domain.model.LocationType as DomainLocationType
import com.mazarini.resa.global.extensions.safeLet
import com.mazarini.resa.global.extensions.set
import com.mazarini.resa.global.preferences.PrefsProvider
import com.mazarini.resa.global.preferences.model.PreferredStop
import com.mazarini.resa.ui.model.Location
import com.mazarini.resa.ui.model.LocationType
import com.mazarini.resa.ui.screens.locationsearch.state.CurrentLocation
import com.mazarini.resa.ui.screens.locationsearch.state.MIN_LENGTH_FOR_SEARCH
import com.mazarini.resa.ui.screens.mapper.DomainToUiLocationMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    val uiState: DeparturesUiState = DeparturesUiState()
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
            is DeparturesUiEvent.QueryByCoordinate -> startQueryJobByCoordinate(event.coordinate, event.locationName)
            is DeparturesUiEvent.UserLocationRequest -> uiState.userLocationRequest.value = event.currentLocation
            is DeparturesUiEvent.PinLocation -> { setPreferredStop(event.location) }
            DeparturesUiEvent.PullRefresh -> pullRefreshDepartures()
            DeparturesUiEvent.DeleteCurrentPreferred -> deleteCurrentPreferred()
            DeparturesUiEvent.StartLocationRequest -> startLocationRequest()
            DeparturesUiEvent.LocationRequestOngoing -> locationFailed()
        }
    }

    private fun pullRefreshDepartures() {
        uiState.isLoading.value = true
        uiState.selectedStop.value?.let { refreshDeparturesForLocation(it) }
    }

    private fun startLocationRequest() {
        uiState.userLocationRequest.value = CurrentLocation.Request
    }

    private fun locationFailed() {
        uiState.userLocationRequest.value = null
        uiState.isLoading.value = false
    }

    private fun deleteCurrentPreferred() {
        viewModelScope.launch {
            uiState.preferredStop.value = null
            prefsProvider.setPreferredDeparture(null)
        }
    }

    private fun getRecentLocations() {
        viewModelScope.launch {
            getRecentLocationsUseCase(
                types = listOf(DomainLocationType.stoparea),
                limit = MAX_RECENT_LOCATION,
            ).collectLatest {
                uiState.recentLocations.value = it.map(locationMapper::map)
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
            uiState.selectedStop.value = location
            refreshPreferredStop()
        }
    }

    private fun initPreferredStop() {
        viewModelScope.launch {
            uiState.preferredStop.value = prefsProvider.getPreferredDeparture()
            uiState.preferredStop.value?.let {
                uiState.isLoading set true
                refreshDeparturesForLocation(Location(id = it.gid, name = it.name, type = LocationType.gps))
            }?: run {
                uiState.requestFocus.value = true
            }
        }
    }

    private fun refreshPreferredStop() {
        viewModelScope.launch {
            uiState.preferredStop.value = prefsProvider.getPreferredDeparture()
        }
    }

    private fun observeSelectedStop() {
        viewModelScope.launch {
            combine(uiState.selectedStop, uiState.preferredStop) { selectedStop, preferredStop ->
                safeLet(selectedStop, preferredStop) { selected, preferred ->
                    selected.id == preferred.gid
                } ?: false
            }.collectLatest {
                uiState.isPreferredSelected.value = it
            }
        }
    }

    private fun onStopSelected(location: Location) {
        uiState.selectedStop.value = location
        uiState.isLoading.value = true
        saveRecentLocation(location)
        refreshDeparturesForLocation(location)
    }

    private fun refreshDeparturesForLocation(location: Location) {
        viewModelScope.launch {
            uiState.selectedStop.value = location
            uiState.departures.value = getStopDeparturesUseCase(location.id).sortedBy { it.platform }
            uiState.isLoading.value = false
        }
    }

    private fun stopQueryChanged(query: String) {
        uiState.selectedStop.value = null
        uiState.stopQuery.value = query
    }

    private fun observeStopQuery() {
        viewModelScope.launch {
            uiState.stopQuery.collectLatest {
                if (!isQueryingByLocation) {
                    uiState.showStopQueryResult.value = false
                    if (it.length >= MIN_LENGTH_FOR_SEARCH) {
                        uiState.showStopQueryResult.value = true
                        startQueryJobByName(it)
                    }
                }
            }
        }
    }

    private fun startQueryJobByName(name: String) {
        stopQueryJob?.cancel()
        uiState.isLoading.value = true
        stopQueryJob = viewModelScope.launch {
            uiState.stopQueryResult.value = queryStopsByName(name).map { locationMapper.map(it) }
            uiState.isLoading.value = false
        }
    }

    private fun startQueryJobByCoordinate(coordinate: Coordinate, locationName: String) {
        stopQueryJob?.cancel()
        uiState.isLoading.value = true
        uiState.showStopQueryResult.value = true
        isQueryingByLocation = true
        uiState.stopQuery.value = locationName
        stopQueryJob = viewModelScope.launch {
            uiState.stopQueryResult.value = queryStopByCoordinate(coordinate).map { locationMapper.map(it) }
            uiState.isLoading.value = false
        }
        isQueryingByLocation = false
    }

    companion object {
        const val MAX_RECENT_LOCATION = 10
    }
}
