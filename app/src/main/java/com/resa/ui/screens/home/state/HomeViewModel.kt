package com.resa.ui.screens.home.state

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resa.data.network.services.RetrofitService
import com.resa.data.network.services.travelplanner.JourneysService
import com.resa.domain.model.Coordinate
import com.resa.domain.model.queryjourneys.QueryJourneysParams
import com.resa.domain.model.queryjourneys.QueryJourneysRelatesTo
import com.resa.domain.usecases.RefreshTokenUseCase
import com.resa.domain.usecases.journey.DeleteSavedJourneySearchUseCase
import com.resa.domain.usecases.journey.GetSavedJourneySearchesUseCase
import com.resa.domain.usecases.journey.SaveCurrentJourneyQueryUseCase
import com.resa.domain.usecases.stoparea.GetDeparturesAroundUseCase
import com.resa.global.extensions.rfc3339
import com.resa.global.preferences.PrefsProvider
import com.resa.ui.model.JourneySearch
import com.resa.ui.screens.home.model.SavedJourneyState
import com.resa.ui.screens.home.model.StopPointsState
import com.resa.ui.screens.home.model.StopPointsState.Companion.filterByDepartures
import com.resa.ui.screens.mapper.DomainToUiJourneySearchMapper
import com.resa.ui.util.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val getSavedJourneySearchesUseCase: GetSavedJourneySearchesUseCase,
    private val saveCurrentJourneyQueryUseCase: SaveCurrentJourneyQueryUseCase,
    private val deleteSavedJourneySearchUseCase: DeleteSavedJourneySearchUseCase,
    private val getDeparturesAroundUseCase: GetDeparturesAroundUseCase,
    private val journeySearchMapper: DomainToUiJourneySearchMapper,
) : ViewModel() {

    val uiState: HomeUiState = HomeUiState()

    init {
        refreshToken()
        loadSavedJourneys()
    }

    private fun refreshToken() {
        viewModelScope.launch {
            refreshTokenUseCase()
        }
    }

    private fun loadDeparturesAround(lat: Double, lon: Double) {
//        uiState.currentLocation.value = Coordinate(lat = 57.679932, lon = 12.014784) // Home
//        uiState.currentLocation.value = Coordinate(lat = 57.706599, lon = 11.968141) // Brunnsparken
//        uiState.currentLocation.value = Coordinate(lat = 53.391628, lon = 36.784628) // Russia
//        uiState.currentLocation.value = Coordinate(lat = 57.708386, lon = 11.972655) // Nordstan-Centrastationen
        uiState.currentLocation.value = Coordinate(lat = lat, lon = lon)
        uiState.stopPoints.value = StopPointsState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            uiState.currentLocation.value?.let { coordinate ->
                getDeparturesAroundUseCase(coordinate)
                    .onSuccess { stopPoints ->
                        Log.e(
                            "HomeViewModel", "VM stopPoints: ${stopPoints.size} " +
                                    "first departures: ${stopPoints.first().departures.size}"
                        )
                        uiState.isDeparturesReloading.value = false
                        stopPoints
                            .filterByDepartures()
                            .takeIf { it.isNotEmpty() }?.let {
                                uiState.stopPoints.value = StopPointsState.Loaded(it)
                            } ?: run {
                            uiState.stopPoints.value = StopPointsState.Error("No departures found")
                        }
                    }.onFailure { throwable ->
                        uiState.isDeparturesReloading.value = false
                        setDeparturesFailed(throwable.message ?: "")
                    }
            } ?: run {
                setDeparturesFailed("No location found")
            }
        }
    }

    private fun setDeparturesFailed(message: String) {
        uiState.stopPoints.value = StopPointsState.Error(message)
    }

    private fun loadSavedJourneys() {
        viewModelScope.launch(Dispatchers.IO) {
            getSavedJourneySearchesUseCase()
                .flowOn(Dispatchers.Main)
                .collectLatest {
                    val journeys = it.map { domainJourneySearch ->
                        journeySearchMapper.map(domainJourneySearch)
                    }
                    uiState.savedJourneyState.value =
                        if (it.isEmpty()) {
                            SavedJourneyState.Empty
                        } else {
                            SavedJourneyState.Loaded(journeys)
                        }
                }
        }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnSavedJourneyClicked -> verifyJourneySearch(event.journeySearch)
            is HomeUiEvent.DeleteSavedJourney -> deleteSavedJourneySearch(event.id)
            HomeUiEvent.NavigationRequested -> uiState.navigateToJourneySelection.value = false
            is HomeUiEvent.UpdateGpsRequest -> updateGpsRequest(event)
            is HomeUiEvent.LocationResult -> executePendingLocationUse(event.lat, event.lon)
            is HomeUiEvent.CheckedPermission -> setCheckPermissionResult(event.hasPermission)
            is HomeUiEvent.RefreshDepartures -> {}//refreshDepartures()
            is HomeUiEvent.ClearLoadingSavedJourneys -> clearLoadingSavedJourneys()
        }
    }

    private fun clearLoadingSavedJourneys() {
        (uiState.savedJourneyState.value as? SavedJourneyState.Loaded)?.let { savedJourneys ->
            val updatedJourneys = savedJourneys.journeys.map { it.copy(isLoading = false) }
            uiState.savedJourneyState.value = SavedJourneyState.Loaded(updatedJourneys)
        }
    }

    private fun refreshDepartures() {
        uiState.isDeparturesReloading.value = true
        uiState.currentLocation.value?.let {
            loadDeparturesAround(it.lat, it.lon)
        }
    }

    private fun updateGpsRequest(event: HomeUiEvent.UpdateGpsRequest) {
        event.pendingLocationUse?.let { uiState.pendingLocationUses.value = it }
        uiState.requestGpsLocation.value = event.request
    }

    private fun setCheckPermissionResult(hasPermission: Boolean) {
        if (hasPermission) {
            uiState.pendingLocationUses.value = PendingLocationUse.SEARCH_DEPARTURES_AROUND
            uiState.requestGpsLocation.value = true
        } else {
            uiState.stopPoints.value = StopPointsState.NeedLocation
        }
        uiState.hasLocationPermission.value = hasPermission
        uiState.hasCheckedPermission.value = true
    }

    private fun executePendingLocationUse(lat: Double, lon: Double) {
        when (uiState.pendingLocationUses.value) {
            PendingLocationUse.SEARCH_SAVED_JOURNEY -> updateJourneySearchWithLocation(lat, lon)
            PendingLocationUse.SEARCH_DEPARTURES_AROUND -> loadDeparturesAround(lat, lon)
            else -> {}
        }
        uiState.hasLocationPermission.value = true
        uiState.pendingLocationUses.value = PendingLocationUse.NONE
    }

    private fun updateJourneySearchWithLocation(lat: Double, lon: Double) {
        uiState.journeySearchRequest.value?.let {
            val updatedSearch = if (it.origin.isGps()) {
                it.copy(origin = it.origin.copy(lat = lat, lon = lon))
            } else {
                it.copy(destination = it.destination.copy(lat = lat, lon = lon))
            }
            searchSavedJourney(updatedSearch)
        }
    }

    private fun deleteSavedJourneySearch(id: Int) {
        viewModelScope.launchIO {
            deleteSavedJourneySearchUseCase(id)
        }
    }

    private fun verifyJourneySearch(journeySearch: JourneySearch) {
        setSavedJourneyLoading(journeySearch.id)
        if (journeySearch.origin.isGps() || journeySearch.destination.isGps()) {
            uiState.pendingLocationUses.value = PendingLocationUse.SEARCH_SAVED_JOURNEY
            uiState.requestGpsLocation.value = true
            uiState.journeySearchRequest.value = journeySearch
        } else {
            searchSavedJourney(journeySearch)
        }
    }

    private fun setSavedJourneyLoading(id: Int) {
        (uiState.savedJourneyState.value as? SavedJourneyState.Loaded)?.let { savedJourneys ->
            val updatedJourneys = savedJourneys.journeys.map {
                if (it.id == id) it.copy(isLoading = true) else it
            }
            uiState.savedJourneyState.value = SavedJourneyState.Loaded(updatedJourneys)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    private fun searchSavedJourney(journeySearch: JourneySearch) {
        val currentJourneyQuery = QueryJourneysParams(
            originGid = journeySearch.origin.id,
            originName = journeySearch.origin.name,
            originLatitude = journeySearch.origin.lat,
            originLongitude = journeySearch.origin.lon,
            originType = journeySearch.origin.type.asJourneyParamsType(),
            destinationGid = journeySearch.destination.id,
            destinationName = journeySearch.destination.name,
            destinationLatitude = journeySearch.destination.lat,
            destinationLongitude = journeySearch.destination.lon,
            destinationType = journeySearch.destination.type.asJourneyParamsType(),
            dateTime = Date().rfc3339(),
            dateTimeRelatesTo = QueryJourneysRelatesTo.DEPARTURE,
        )
        viewModelScope.launch {
            saveCurrentJourneyQueryUseCase(currentJourneyQuery)
            uiState.navigateToJourneySelection.value = true
        }
    }

    enum class PendingLocationUse {
        NONE,
        SEARCH_SAVED_JOURNEY,
        SEARCH_DEPARTURES_AROUND,
    }
}
