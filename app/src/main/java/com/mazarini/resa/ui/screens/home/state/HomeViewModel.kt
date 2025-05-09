package com.mazarini.resa.ui.screens.home.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysParams
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysRelatesTo
import com.mazarini.resa.domain.usecases.RefreshTokenUseCase
import com.mazarini.resa.domain.usecases.journey.DeleteRecentJourneySearchUseCase
import com.mazarini.resa.domain.usecases.journey.DeleteSavedJourneySearchUseCase
import com.mazarini.resa.domain.usecases.journey.GetRecentJourneySearchesUseCase
import com.mazarini.resa.domain.usecases.journey.GetSavedJourneySearchesUseCase
import com.mazarini.resa.domain.usecases.journey.LoadSavedJourneyToHomeUseCase
import com.mazarini.resa.domain.usecases.journey.SaveCurrentJourneyQueryUseCase
import com.mazarini.resa.global.extensions.minusMinutes
import com.mazarini.resa.global.extensions.rfc3339
import com.mazarini.resa.global.model.ThemeSettings
import com.mazarini.resa.global.preferences.PrefsProvider
import com.mazarini.resa.ui.model.JourneySearch
import com.mazarini.resa.ui.screens.home.model.SavedJourneyState
import com.mazarini.resa.ui.screens.home.model.StopPointsState
import com.mazarini.resa.ui.screens.mapper.DomainToUiJourneySearchMapper
import com.mazarini.resa.ui.util.copyUpdate
import com.mazarini.resa.ui.util.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val deleteRecentJourneySearchUseCase: DeleteRecentJourneySearchUseCase,
    private val getRecentJourneySearchesUseCase: GetRecentJourneySearchesUseCase,
    private val journeySearchMapper: DomainToUiJourneySearchMapper,
    private val loadSavedJourneyToHomeUseCase: LoadSavedJourneyToHomeUseCase,
    private val prefsProvider: PrefsProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        refreshToken()
        loadSavedJourneys()
        loadRecentJourneys()
        loadCurrentLanguage()
        loadCurrentTheme()
        checkPinnedHomeJourney()
        checkOnboarding()
    }

    private fun checkPinnedHomeJourney() {
        viewModelScope.launch {
            prefsProvider.getSavedJourney()?.let { journey ->
                validateSavedJourney(journey)
            } ?: run {
                _uiState.copyUpdate { copy(pinnedHomeJourney = null) }
            }
        }
    }

    private fun loadCurrentLanguage() {
        viewModelScope.launch {
            prefsProvider.getLanguage().let { language ->
                _uiState.copyUpdate { copy(currentLanguage = language ?: "en") }
            }
        }
    }

    private fun loadCurrentTheme() {
        viewModelScope.launch {
            prefsProvider.getThemeSettings().let { theme ->
                _uiState.copyUpdate { copy(currentTheme = theme) }
            }
        }
    }

    private fun checkOnboarding() {
        viewModelScope.launch {
            prefsProvider.hasSeenOnboarding().let { shown ->
                _uiState.copyUpdate { copy(showOnboarding = !shown) }
            }
            prefsProvider.setSeenOnboarding()
        }
    }

    private fun validateSavedJourney(journey: Journey) {
        val hasFinished = journey.arrivalTimes.arrival().before(Date().minusMinutes(60))
        if (hasFinished) {
            clearPinnedJourney()
        } else {
            _uiState.copyUpdate { copy(pinnedHomeJourney = journey) }
        }
    }

    private fun clearPinnedJourney() {
        _uiState.copyUpdate { copy(pinnedHomeJourney = null) }
        viewModelScope.launch {
            prefsProvider.setSavedJourney(null)
        }
    }

    private fun refreshToken() {
        viewModelScope.launch {
            refreshTokenUseCase()
        }
    }

    private fun loadDeparturesAround(lat: Double, lon: Double) {
        _uiState.copyUpdate {
            copy(
                currentLocation = Coordinate(lat = lat, lon = lon),
                stopPoints = StopPointsState.Loading,
            )
        }
    }

    private fun loadSavedJourneys() {
        viewModelScope.launch {
            getSavedJourneySearchesUseCase().flowOn(Dispatchers.IO).collectLatest { savedSearch ->
                val journeys = savedSearch.map { domainJourneySearch ->
                    journeySearchMapper.map(domainJourneySearch)
                }
                _uiState.copyUpdate {
                    copy(
                        savedJourneyState = if (savedSearch.isEmpty()) {
                            SavedJourneyState.Empty
                        } else {
                            SavedJourneyState.Loaded(journeys)
                        }
                    )
                }
            }
        }
    }

    private fun loadRecentJourneys() {
        viewModelScope.launch {
            getRecentJourneySearchesUseCase().flowOn(Dispatchers.IO).collectLatest { recentSearch ->
                val journeys = recentSearch.map { domainJourneySearch ->
                    journeySearchMapper.map(domainJourneySearch)
                }
                _uiState.copyUpdate {
                    copy(
                        recentJourneysState = if (recentSearch.isEmpty()) {
                            SavedJourneyState.Empty
                        } else {
                            SavedJourneyState.Loaded(journeys)
                        }
                    )
                }
            }
        }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnSavedJourneyClicked -> verifyJourneySearch(event.journeySearch)
            is HomeUiEvent.DeleteSavedJourney -> deleteSavedJourneySearch(event.id)
            HomeUiEvent.NavigationRequested -> _uiState.copyUpdate { copy(navigateToJourneySelection = false) }
            is HomeUiEvent.UpdateGpsRequest -> updateGpsRequest(event)
            is HomeUiEvent.LocationResult -> executePendingLocationUse(event.lat, event.lon)
            is HomeUiEvent.CheckedPermission -> setCheckPermissionResult(event.hasPermission)
            is HomeUiEvent.ClearLoadingSavedJourneys -> clearLoadingSavedJourneys()
            is HomeUiEvent.DeleteRecentJourney -> deleteRecentJourneySearch(event.id)
            HomeUiEvent.LoadSavedJourneyToHome -> loadSavedJourneyToHome()
            HomeUiEvent.DeleteSavedJourneyToHome -> clearPinnedJourney()
            HomeUiEvent.CheckSavedJourneyToHome -> checkPinnedHomeJourney()
            is HomeUiEvent.SetLanguage -> setLanguage(event.language)
            HomeUiEvent.OnboardShown -> _uiState.copyUpdate { copy(showOnboarding = false) }
            is HomeUiEvent.OnThemeChanged -> updateThemeSettings(event.themeSettings)
        }
    }

    private fun loadSavedJourneyToHome() {
        viewModelScope.launchIO {
            loadSavedJourneyToHomeUseCase()
        }
    }

    private fun updateThemeSettings(themeSettings: ThemeSettings) {
        viewModelScope.launchIO {
            prefsProvider.setThemeSettings(themeSettings)
            _uiState.copyUpdate { copy(currentTheme = themeSettings) }
        }
    }
    private fun setLanguage(language: String) {
        viewModelScope.launch {
            prefsProvider.setLanguage(language)
        }
    }

    private fun clearLoadingSavedJourneys() {
        (_uiState.value.savedJourneyState as? SavedJourneyState.Loaded)?.let { savedJourneys ->
            val updatedJourneys = savedJourneys.journeys.map { it.copy(isLoading = false) }
            _uiState.copyUpdate { copy(savedJourneyState = SavedJourneyState.Loaded(updatedJourneys)) }
        }
        (_uiState.value.recentJourneysState as? SavedJourneyState.Loaded)?.let { savedJourneys ->
            val updatedJourneys = savedJourneys.journeys.map { it.copy(isLoading = false) }
            _uiState.copyUpdate { copy(recentJourneysState = SavedJourneyState.Loaded(updatedJourneys)) }
        }
    }

    private fun updateGpsRequest(event: HomeUiEvent.UpdateGpsRequest) {
        _uiState.copyUpdate {
            copy(
                pendingLocationUses = event.pendingLocationUse ?: pendingLocationUses,
                requestGpsLocation = event.request,
            )
        }
    }


    private fun setCheckPermissionResult(hasPermission: Boolean) {
        _uiState.copyUpdate {
            copy(
                requestGpsLocation = if (hasPermission) true else requestGpsLocation,
                pendingLocationUses = if (hasPermission) PendingLocationUse.SEARCH_DEPARTURES_AROUND else pendingLocationUses,
                stopPoints = if (!hasPermission) StopPointsState.NeedLocation else stopPoints,
                hasLocationPermission = hasPermission,
                hasCheckedPermission = true,
            )
        }
    }


    private fun executePendingLocationUse(lat: Double, lon: Double) {
        when (_uiState.value.pendingLocationUses) {
            PendingLocationUse.SEARCH_SAVED_JOURNEY -> updateJourneySearchWithLocation(lat, lon)
            PendingLocationUse.SEARCH_DEPARTURES_AROUND -> loadDeparturesAround(lat, lon)
            else -> {}
        }
        _uiState.copyUpdate {
            copy(
                hasLocationPermission = true,
                pendingLocationUses = PendingLocationUse.NONE,
            )
        }
    }

    private fun updateJourneySearchWithLocation(lat: Double, lon: Double) {
        _uiState.value.journeySearchRequest?.let {
            val updatedSearch = if (it.origin.isGps()) {
                it.copy(origin = it.origin.copy(lat = lat, lon = lon))
            } else {
                it.copy(destination = it.destination.copy(lat = lat, lon = lon))
            }
            searchSavedJourney(updatedSearch)
        }
    }

    private fun deleteSavedJourneySearch(id: String) {
        viewModelScope.launchIO {
            deleteSavedJourneySearchUseCase(id)
        }
    }

    private fun deleteRecentJourneySearch(id: String) {
        viewModelScope.launchIO {
            deleteRecentJourneySearchUseCase(id)
        }
    }

    private fun verifyJourneySearch(journeySearch: JourneySearch) {
        setSavedJourneyLoading(journeySearch.id)
        if (journeySearch.origin.isGps() || journeySearch.destination.isGps()) {
            _uiState.copyUpdate {
                copy(
                    requestGpsLocation = true,
                    pendingLocationUses = PendingLocationUse.SEARCH_SAVED_JOURNEY,
                    journeySearchRequest = journeySearch,
                )
            }
        } else {
            clearLoadingSavedJourneys()
            searchSavedJourney(journeySearch)
        }
    }

    private fun setSavedJourneyLoading(id: String) {
        (uiState.value.savedJourneyState as? SavedJourneyState.Loaded)?.let { savedJourneys ->
            val updatedJourneys = savedJourneys.journeys.map {
                if (it.id == id) it.copy(isLoading = true) else it
            }
            _uiState.copyUpdate { copy(savedJourneyState = SavedJourneyState.Loaded(updatedJourneys)) }
        }
        (uiState.value.recentJourneysState as? SavedJourneyState.Loaded)?.let { savedJourneys ->
            val updatedJourneys = savedJourneys.journeys.map {
                if (it.id == id) it.copy(isLoading = true) else it
            }
            _uiState.copyUpdate { copy(recentJourneysState = SavedJourneyState.Loaded(updatedJourneys)) }
        }
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
            _uiState.copyUpdate { copy(navigateToJourneySelection = true) }
        }
    }

    enum class PendingLocationUse {
        NONE,
        SEARCH_SAVED_JOURNEY,
        SEARCH_DEPARTURES_AROUND,
    }
}
