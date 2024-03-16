package com.resa.ui.screens.journeyselection.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resa.domain.model.JourneySearch
import com.resa.domain.model.journey.Journey
import com.resa.domain.model.queryjourneys.QueryJourneysParams
import com.resa.domain.model.queryjourneys.QueryJourneysRelatesTo.*
import com.resa.domain.usecases.journey.GetCurrentJourneyQueryUseCase
import com.resa.domain.usecases.journey.QueryJourneysUseCase
import com.resa.domain.usecases.journey.QueryPassedJourneysUseCase
import com.resa.domain.usecases.journey.SaveCurrentJourneyQueryUseCase
import com.resa.domain.usecases.journey.SaveJourneySearchUseCase
import com.resa.domain.usecases.journey.SetSelectedJourneyUseCase
import com.resa.global.extensions.parseRfc3339
import com.resa.global.extensions.rfc3339
import com.resa.global.extensions.setDateOnly
import com.resa.global.extensions.setHourMinute
import com.resa.global.logd
import com.resa.ui.screens.journeyselection.state.JourneySelectionUiEvent.*
import com.resa.ui.screens.locationsearch.model.JourneyFilters
import com.resa.ui.util.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import com.resa.domain.model.Location as DomainLocation
import com.resa.domain.model.LocationType as DomainLocationType

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
) : ViewModel() {

    val uiState: JourneySelectionUiState = JourneySelectionUiState()

    init {
        queryJourneys()
        queryPassedJourneys()
        loadCurrentQueryParams()
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

    private fun updateFilters(queryParams: QueryJourneysParams) {
        uiState.journeyFilters.value = JourneyFilters(
            date = queryParams.dateTime?.parseRfc3339() ?: Date(),
            isDepartureFilters = queryParams.dateTimeRelatesTo == DEPARTURE,
        )
    }

    private fun queryJourneys() {
        viewModelScope.launch {
            uiState.upcomingJourneys.value = queryJourneysUseCase()
        }
    }

    private fun queryPassedJourneys() {
        viewModelScope.launch {
            uiState.passedJourneys.value = queryPassedJourneysUseCase()
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
            with(uiState.journeyFilters.value) {
                val newParams = uiState.queryParams.value.copy(
                    dateTime = date.rfc3339(),
                    dateTimeRelatesTo = if (isDepartureFilters) DEPARTURE else ARRIVAL,
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
                        id = 0,
                        origin = origin,
                        destination = destination,
                    )
                )
            }
        }
    }

    private fun setDateFilter(newDate: Date) {
        uiState.filtersChanged.value = true
        uiState.journeyFilters.value = with(uiState.journeyFilters.value) {
            copy(
                date = this.date.setDateOnly(newDate),
            )
        }
    }

    private fun setTimeFilter(newTime: Date) {
        uiState.filtersChanged.value = true
        uiState.journeyFilters.value = with(uiState.journeyFilters.value) {
            copy(
                date = this.date.setHourMinute(newTime),
            )
        }
    }

    private fun setIsDepartureFilter(isDeparture: Boolean) {
        uiState.filtersChanged.value = true
        uiState.journeyFilters.value = uiState.journeyFilters.value.copy(
            isDepartureFilters = isDeparture
        )
    }

    private companion object {
        const val TAG = "JourneySelectionViewModel"
    }
}
