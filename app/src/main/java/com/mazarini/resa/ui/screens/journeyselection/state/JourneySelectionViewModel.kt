package com.mazarini.resa.ui.screens.journeyselection.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mazarini.resa.domain.model.JourneySearch
import com.mazarini.resa.domain.model.TransportMode
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysParams
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysRelatesTo.ARRIVAL
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysRelatesTo.DEPARTURE
import com.mazarini.resa.domain.usecases.journey.GetCurrentJourneyQueryUseCase
import com.mazarini.resa.domain.usecases.journey.QueryJourneysUseCase
import com.mazarini.resa.domain.usecases.journey.QueryPassedJourneysUseCase
import com.mazarini.resa.domain.usecases.journey.SaveCurrentJourneyQueryUseCase
import com.mazarini.resa.domain.usecases.journey.SaveJourneySearchUseCase
import com.mazarini.resa.domain.usecases.journey.SetSelectedJourneyUseCase
import com.mazarini.resa.global.analytics.logd
import com.mazarini.resa.global.extensions.parseRfc3339
import com.mazarini.resa.global.extensions.rfc3339
import com.mazarini.resa.global.extensions.setHourMinute
import com.mazarini.resa.global.preferences.PrefsProvider
import com.mazarini.resa.ui.screens.journeyselection.state.JourneySelectionUiEvent.DateFilterChanged
import com.mazarini.resa.ui.screens.journeyselection.state.JourneySelectionUiEvent.IsDepartFilterChanged
import com.mazarini.resa.ui.screens.journeyselection.state.JourneySelectionUiEvent.JourneySelected
import com.mazarini.resa.ui.screens.journeyselection.state.JourneySelectionUiEvent.SaveCurrentJourneySearch
import com.mazarini.resa.ui.screens.journeyselection.state.JourneySelectionUiEvent.TimeFilterChanged
import com.mazarini.resa.ui.screens.journeyselection.state.JourneySelectionUiEvent.UpdateJourneySearch
import com.mazarini.resa.ui.screens.locationsearch.model.JourneyFilters
import com.mazarini.resa.ui.util.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import com.mazarini.resa.domain.model.Location as DomainLocation
import com.mazarini.resa.domain.model.LocationType as DomainLocationType

@HiltViewModel
class JourneySelectionViewModel
@Inject
constructor(
    private val queryJourneysUseCase: QueryJourneysUseCase,
    private val queryPassedJourneysUseCase: QueryPassedJourneysUseCase,
    private val getCurrentJourneyQueryUseCase: GetCurrentJourneyQueryUseCase,
    private val saveJourneySearchUseCase: SaveJourneySearchUseCase,
    private val saveCurrentJourneyQueryUseCase: SaveCurrentJourneyQueryUseCase,
    private val setSelectedJourneyUseCase: SetSelectedJourneyUseCase,
    private val prefsProvider: PrefsProvider,
) : ViewModel() {

    private val _uiState: MutableStateFlow<JourneySelectionUiState> =
        MutableStateFlow(JourneySelectionUiState())
    val uiState: StateFlow<JourneySelectionUiState> = _uiState

    init {
        queryJourneys()
        queryPassedJourneys()
        loadCurrentQueryParams()
        loadCurrentPreferredModes()
        checkFeatureDiscovery()
    }

    private fun loadCurrentQueryParams() {
        viewModelScope.launch {
            getCurrentJourneyQueryUseCase()
                .onSuccess { queryParams ->
                    _uiState.update { it.copy(queryParams = queryParams) }
                    updateFilters(queryParams)
                }
        }
    }

    private fun loadCurrentPreferredModes() {
        viewModelScope.launch {
            prefsProvider.collectPreferredTransportModes().collectLatest { filter ->
                _uiState.update {
                    it.copy(
                        filterUiState = it.filterUiState.copy(
                            preferredModes = filter
                        )
                    )
                }
            }
        }
    }

    private fun updateFilters(queryParams: QueryJourneysParams) {
        _uiState.update {
            it.copy(
                filterUiState = it.filterUiState.copy(
                    filters = JourneyFilters(
                        date = queryParams.dateTime?.parseRfc3339() ?: Date(),
                        isDepartureFilters = queryParams.dateTimeRelatesTo == DEPARTURE,
                    )
                )
            )
        }
    }

    private fun queryJourneys() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    upcomingJourneys = queryJourneysUseCase().cachedIn(viewModelScope)
                )
            }
        }
    }

    private fun queryPassedJourneys() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    passedJourneys = queryPassedJourneysUseCase().cachedIn(viewModelScope)
                )
            }
        }
    }

    fun onEvent(event: JourneySelectionUiEvent) {
        logd(className = TAG, "onEvent:  ${event::class.simpleName}")
        when (event) {
            is IsDepartFilterChanged -> setIsDepartureFilter(isDeparture = event.isDepart)
            is DateFilterChanged -> setDateFilter(event.date)
            is TimeFilterChanged -> setTimeFilter(event.date)
            SaveCurrentJourneySearch -> saveCurrentJourneySearch()
            UpdateJourneySearch -> updateJourneySearch()
            is JourneySelected -> setJourneySelected(event.journey)
            is JourneySelectionUiEvent.OnLegMapClicked -> {}
            is JourneySelectionUiEvent.TransportModesChanged -> updatePreferredModes(event.modes)
        }
    }

    private fun setJourneySelected(journey: Journey) {
        setSelectedJourneyUseCase(
            journey.copy(
                originName = _uiState.value.queryParams.originName,
                destName = uiState.value.queryParams.destinationName,
            )
        )
    }

    private fun updateJourneySearch() {
        if (_uiState.value.filtersChanged) {
            _uiState.update { it.copy(filtersChanged = false) }
            with(_uiState.value.filterUiState) {
                val newParams = _uiState.value.queryParams.copy(
                    dateTime = this.filters.date.rfc3339(),
                    dateTimeRelatesTo = if (this.filters.isDepartureFilters) DEPARTURE else ARRIVAL,
                    transportModes = this.preferredModes + TransportMode.walk,
                )
                viewModelScope.launchIO {
                    saveCurrentJourneyQueryUseCase(newParams)
                    queryJourneys()
                    queryPassedJourneys()
                }
            }
        }
    }

    private fun saveCurrentJourneySearch() {
        with(_uiState.value.queryParams) {
            val origin = DomainLocation(
                id = originGid.orEmpty(),
                name = originName.orEmpty(),
                lat = originLatitude,
                lon = originLongitude,
                type = originType ?: DomainLocationType.unknown,
            )
            val destination = DomainLocation(
                id = destinationGid.orEmpty(),
                name = destinationName.orEmpty(),
                lat = destinationLatitude,
                lon = destinationLongitude,
                type = destinationType ?: DomainLocationType.unknown,
            )
            viewModelScope.launchIO {
                saveJourneySearchUseCase(
                    JourneySearch(
                        id = origin.latLng() + destination.latLng(),
                        origin = origin,
                        destination = destination,
                    )
                )
            }
        }
    }

    private fun setDateFilter(newDate: Date) {
        _uiState.update {
            it.copy(
                filtersChanged = true,
                filterUiState = it.filterUiState.copy(
                    filters = it.filterUiState.filters.copy(date = newDate)
                )
            )
        }
    }

    private fun setTimeFilter(newTime: Date) {
        _uiState.update {
            it.copy(
                filtersChanged = true,
                filterUiState = it.filterUiState.copy(
                    filters = it.filterUiState.filters.copy(
                        date = it.filterUiState.filters.date.setHourMinute(
                            newTime
                        )
                    )
                )
            )
        }
    }

    private fun setIsDepartureFilter(isDeparture: Boolean) {
        _uiState.update {
            it.copy(
                filtersChanged = true,
                filterUiState = it.filterUiState.copy(
                    filters = it.filterUiState.filters.copy(isDepartureFilters = isDeparture)
                )
            )
        }
    }

    private fun updatePreferredModes(modes: List<TransportMode>) {
        viewModelScope.launch {
            prefsProvider.setPreferredTransportModes(modes)
            _uiState.update { it.copy(filtersChanged = true) }
        }
    }

    private fun checkFeatureDiscovery() {
        viewModelScope.launch {
            val hasSeen = prefsProvider.getUserPrefs().hasSeenSaveQueryFeat
            if (hasSeen.not()) {
                prefsProvider.setSeenSaveQueryFeat()
                _uiState.update { it.copy(showFeatureHighlight = true) }
            }
        }
    }

    private companion object {
        const val TAG = "JourneySelectionViewModel"
    }
}
