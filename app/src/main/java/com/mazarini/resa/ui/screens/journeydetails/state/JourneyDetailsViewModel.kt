package com.mazarini.resa.ui.screens.journeydetails.state

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazarini.resa.domain.model.VehiclePosition
import com.mazarini.resa.domain.model.id
import com.mazarini.resa.domain.usecases.journey.CheckHasVehiclePositionUseCase
import com.mazarini.resa.domain.usecases.journey.FetchSelectedJourneyDetailsUseCase
import com.mazarini.resa.domain.usecases.journey.GetSelectedJourneyUseCase
import com.mazarini.resa.domain.usecases.journey.ListenVehiclePositionUseCase
import com.mazarini.resa.domain.usecases.journey.LoadSavedJourneyToHomeUseCase
import com.mazarini.resa.domain.usecases.journey.SaveCurrentJourneyToHomeUseCase
import com.mazarini.resa.domain.usecases.journey.StartVehicleTrackingUseCase
import com.mazarini.resa.global.analytics.logd
import com.mazarini.resa.global.analytics.loge
import com.mazarini.resa.global.preferences.PrefsProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JourneyDetailsViewModel
@Inject
constructor(
    private val getSelectedJourneyUseCase: GetSelectedJourneyUseCase,
    private val fetchSelectedJourneyDetailsUseCase: FetchSelectedJourneyDetailsUseCase,
    private val listenVehiclePositionUseCase: ListenVehiclePositionUseCase,
    private val startVehicleTrackingUseCase: StartVehicleTrackingUseCase,
    private val checkHasVehiclePositionUseCase: CheckHasVehiclePositionUseCase,
    private val saveCurrentJourneyToHomeUseCase: SaveCurrentJourneyToHomeUseCase,
    private val prefsProvider: PrefsProvider,
) : ViewModel() {

    val uiState: JourneyDetailsUiState = JourneyDetailsUiState()

    init {
        getSelectedJourney()
        checkHasVehiclePosition()
    }

    private fun getSelectedJourney() {
        viewModelScope.launch {
            fetchSelectedJourneyDetailsUseCase()
            getSelectedJourneyUseCase().collect { result ->
                result?.let {
                    loge("JourneyDetailsViewModel: getSelectedJourney: $it")
                    uiState.selectedJourney.value = it
                    checkHomeJourney()
                }
            }
        }
    }

    private fun checkHomeJourney() {
        viewModelScope.launch {
            prefsProvider.getSavedJourney()?.let {
                if (it.id == uiState.selectedJourney.value?.id) {
                    uiState.isCurrentJourneyAddedHome.value = true
                }
            }
        }
    }

    private fun checkHasVehiclePosition() {
        viewModelScope.launch {
            checkHasVehiclePositionUseCase()
                .onSuccess {
                    logd("JourneyDetailsViewModel", "checkHasVehiclePosition: $it")
                    uiState.isTrackingAvailable.value = it
                }.onFailure {
                    loge("JourneyDetailsViewModel: checkHasVehiclePosition: $it")
                    uiState.isTrackingAvailable.value = false
                }
        }
    }

    fun onEvent(event: JourneyDetailsUiEvent) {
        when (event) {
            is JourneyDetailsUiEvent.LocationAccessResult -> setLocationAccessResult(event.hasAccess)
            is JourneyDetailsUiEvent.SetShouldShowMap -> uiState.shouldShowMap.value = event.show
            is JourneyDetailsUiEvent.StartTrackingVehicles -> startTrackingVehicles()
            is JourneyDetailsUiEvent.OnFollowClicked -> onFollowClicked(event.vehiclePosition)
            JourneyDetailsUiEvent.StopFollowingVehicle -> uiState.followingVehicle.value = null
            JourneyDetailsUiEvent.SaveJourneyToHome -> saveJourneyToHome()
            else -> loge("JourneyDetailsViewModel: onEvent: $event")
        }
    }

    private fun onFollowClicked(vehiclePosition: VehiclePosition) {
        viewModelScope.launch {
            if (uiState.followingVehicle.value?.id != vehiclePosition.id)
                uiState.followingVehicle.value = vehiclePosition
            else uiState.followingVehicle.value = null
        }
    }

    private fun startTrackingVehicles() {
        viewModelScope.launch {
            startVehicleTrackingUseCase()
        }
        viewModelScope.launch {
            listenVehiclePositionUseCase().collect { result ->
                uiState.trackedVehicles.value = result
                checkForFollowingVehicle(result)
            }
        }
    }

    private fun saveJourneyToHome() {
        uiState.isCurrentJourneyAddedHome.value = true
        viewModelScope.launch {
            saveCurrentJourneyToHomeUseCase()
        }
    }
    private fun checkForFollowingVehicle(result: List<VehiclePosition>) {
        uiState.followingVehicle.value?.let {
            result.forEach {
                if (it.id == uiState.followingVehicle.value?.id) {
                    uiState.followingVehicle.value = it
                    return@let
                }
            }
            uiState.followingVehicle.value = null
        }
    }

    private fun setLocationAccessResult(hasAccess: Boolean) {
        uiState.hasLocationAccess.value = hasAccess
    }

//    private fun filterStopsInBetween(leg: Leg, data: List<LegStop>) {
//        var originFound = false
//        val destStopId = leg.destStopId
//        val originStopId = leg.originStopId
//        val stops = mutableListOf<LegStop>()
//        run breaking@{
//            data.forEach {
//                if (it.id == destStopId) return@breaking
//                if (originFound) {
//                    stops.add(it)
//                }
//                if (it.id == originStopId) originFound = true
//            }
//        }
//        leg.legStops = stops
//        uiState.shouldRecompose.value = true
//    }
}
