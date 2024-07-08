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
import com.mazarini.resa.global.extensions.parseRfc3339
import com.mazarini.resa.global.extensions.rfc3339
import com.mazarini.resa.global.extensions.setHourMinute
import com.mazarini.resa.global.analytics.logd
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
import kotlinx.coroutines.flow.collectLatest
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

    val uiState: JourneySelectionUiState = JourneySelectionUiState()

    init {
        queryJourneys()
        queryPassedJourneys()
        loadCurrentQueryParams()
        loadCurrentPreferredModes()
    }

    private fun loadCurrentQueryParams() {
        viewModelScope.launch {
            getCurrentJourneyQueryUseCase()
                .onSuccess { queryParams ->
                    uiState.queryParams.value = queryParams
                    updateFilters(queryParams)
                }
        }
    }

    private fun loadCurrentPreferredModes() {
        viewModelScope.launch {
            prefsProvider.collectPreferredTransportModes().collectLatest {
                uiState.filterUiState.preferredModes.value = it
            }
        }
    }

    private fun updateFilters(queryParams: QueryJourneysParams) {
        uiState.filterUiState.filters.value = JourneyFilters(
            date = queryParams.dateTime?.parseRfc3339() ?: Date(),
            isDepartureFilters = queryParams.dateTimeRelatesTo == DEPARTURE,
        )
    }

    private fun queryJourneys() {
        viewModelScope.launch {
            uiState.upcomingJourneys.value = queryJourneysUseCase().cachedIn(viewModelScope)
        }
    }

    private fun queryPassedJourneys() {
        viewModelScope.launch {
            uiState.passedJourneys.value = queryPassedJourneysUseCase().cachedIn(viewModelScope)
        }
    }

    fun onEvent(event: JourneySelectionUiEvent) {
        logd(className = TAG, "onEvent: ")
        when (event) {
            is IsDepartFilterChanged -> setIsDepartureFilter(isDeparture = event.isDepart)
            is DateFilterChanged -> setDateFilter(event.date)
            is TimeFilterChanged -> setTimeFilter(event.date)
            SaveCurrentJourneySearch -> saveCurrentJourneySearch()
            UpdateJourneySearch -> updateJourneySearch()
            is JourneySelected -> setJourneySelected(event.journey)
            is JourneySelectionUiEvent.OnLegMapClicked -> {}
            is JourneySelectionUiEvent.TransportModesChanged -> { uiState.filtersChanged.value = true }
        }
    }

    private fun setJourneySelected(journey: Journey) {
        setSelectedJourneyUseCase(
            journey.copy(
                originName = uiState.queryParams.value.originName,
                destName = uiState.queryParams.value.destinationName,
            )
        )
    }

    private fun updateJourneySearch() {
        if (uiState.filtersChanged.value) {
            uiState.filtersChanged.value = false
            with(uiState.filterUiState.filters.value) {
                val newParams = uiState.queryParams.value.copy(
                    dateTime = date.rfc3339(),
                    dateTimeRelatesTo = if (isDepartureFilters) DEPARTURE else ARRIVAL,
                    transportModes = uiState.filterUiState.preferredModes.value + TransportMode.walk,
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
        with(uiState.queryParams.value) {
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
        uiState.filtersChanged.value = true
        uiState.filterUiState.filters.value = with(uiState.filterUiState.filters.value) {
            copy(
                date = newDate,
            )
        }
    }

    private fun setTimeFilter(newTime: Date) {
        uiState.filtersChanged.value = true
        uiState.filterUiState.filters.value = with(uiState.filterUiState.filters.value) {
            copy(
                date = this.date.setHourMinute(newTime),
            )
        }
    }

    private fun setIsDepartureFilter(isDeparture: Boolean) {
        uiState.filtersChanged.value = true
        uiState.filterUiState.filters.value = uiState.filterUiState.filters.value.copy(
            isDepartureFilters = isDeparture
        )
    }

    private companion object {
        const val TAG = "JourneySelectionViewModel"
    }
}
