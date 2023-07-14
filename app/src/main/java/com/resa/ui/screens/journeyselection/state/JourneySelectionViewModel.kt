package com.resa.ui.screens.journeyselection.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resa.domain.model.JourneySearch
import com.resa.domain.usecases.journey.GetCurrentJourneyQueryUseCase
import com.resa.domain.usecases.journey.QueryJourneysUseCase
import com.resa.domain.usecases.journey.SaveJourneySearchUseCase
import com.resa.global.logd
import com.resa.ui.util.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.resa.domain.model.Location as DomainLocation
import com.resa.domain.model.LocationType as DomainLocationType

@HiltViewModel
class JourneySelectionViewModel
@Inject
constructor(
    private val queryJourneysUseCase: QueryJourneysUseCase,
    private val getCurrentJourneyQueryUseCase: GetCurrentJourneyQueryUseCase,
    private val saveJourneySearchUseCase: SaveJourneySearchUseCase,
) : ViewModel() {

    val uiState: JourneySelectionUiState = JourneySelectionUiState()

    init {
        viewModelScope.launch {
            queryJourneys()
            loadCurrentQueryParams()
        }
    }

    private fun loadCurrentQueryParams() {
        viewModelScope.launch {
            getCurrentJourneyQueryUseCase()
                .onSuccess { queryParams ->
                    uiState.queryParams.value = queryParams
                }
        }
    }

    private suspend fun queryJourneys() {
        uiState.journeysResult.value = queryJourneysUseCase()
    }

    fun onEvent(event: JourneySelectionUiEvent) {
        logd(className = TAG, "onEvent: ")
        when (event) {
            is JourneySelectionUiEvent.DateFilterChanged -> TODO()
            is JourneySelectionUiEvent.IsDepartFilterChanged -> TODO()
            is JourneySelectionUiEvent.TimeFilterChanged -> TODO()
            JourneySelectionUiEvent.SaveCurrentJourneySearch -> saveCurrentJourneySearch()
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

    private companion object {
        const val TAG = "JourneySelectionViewModel"
    }
}
