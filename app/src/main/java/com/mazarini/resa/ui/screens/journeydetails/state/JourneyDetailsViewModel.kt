package com.mazarini.resa.ui.screens.journeydetails.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazarini.resa.domain.model.VehiclePosition
import com.mazarini.resa.domain.model.id
import com.mazarini.resa.domain.usecases.journey.CheckHasVehiclePositionUseCase
import com.mazarini.resa.domain.usecases.journey.FetchSelectedJourneyDetailsUseCase
import com.mazarini.resa.domain.usecases.journey.GetSelectedJourneyUseCase
import com.mazarini.resa.domain.usecases.journey.ListenVehiclePositionUseCase
import com.mazarini.resa.domain.usecases.journey.SaveCurrentJourneyToHomeUseCase
import com.mazarini.resa.domain.usecases.journey.StartVehicleTrackingUseCase
import com.mazarini.resa.global.analytics.logd
import com.mazarini.resa.global.analytics.loge
import com.mazarini.resa.ui.util.copyUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(JourneyDetailsUiState())
    val uiState: StateFlow<JourneyDetailsUiState> = _uiState

    init {
        getSelectedJourney()
        checkHasVehiclePosition()
    }

    private fun getSelectedJourney() {
        viewModelScope.launch {
            fetchSelectedJourneyDetailsUseCase()
            getSelectedJourneyUseCase().collect { result ->
                result?.let {
                    _uiState.copyUpdate { copy(selectedJourney = result) }
                    logd(TAG, "JourneyDetailsViewModel: getSelectedJourney: $it")
                }
            }
        }
    }

    private fun checkHasVehiclePosition() {
        viewModelScope.launch {
            checkHasVehiclePositionUseCase()
                .onSuccess {
                    _uiState.copyUpdate { copy(isTrackingAvailable = it) }
                    logd(TAG, "checkHasVehiclePosition: $it")
                }.onFailure {
                    _uiState.copyUpdate { copy(isTrackingAvailable = false) }
                    loge("JourneyDetailsViewModel: checkHasVehiclePosition: $it")
                }
        }
    }

    fun onEvent(event: JourneyDetailsUiEvent) {
        when (event) {
            is JourneyDetailsUiEvent.LocationAccessResult -> setLocationAccessResult(event.hasAccess)
            is JourneyDetailsUiEvent.SetShouldShowMap ->
                _uiState.copyUpdate { copy(shouldShowMap = event.show) }

            is JourneyDetailsUiEvent.StartTrackingVehicles -> startTrackingVehicles()
            is JourneyDetailsUiEvent.OnFollowClicked -> onFollowClicked(event.vehiclePosition)
            JourneyDetailsUiEvent.StopFollowingVehicle -> _uiState.copyUpdate {
                copy(
                    followingVehicle = null
                )
            }

            JourneyDetailsUiEvent.SaveJourneyToHome -> saveJourneyToHome()
            else -> loge("JourneyDetailsViewModel: onEvent: $event")
        }
    }

    private fun onFollowClicked(vehiclePosition: VehiclePosition) {
        val current = _uiState.value.followingVehicle?.id
        val new = if (current == vehiclePosition.id) null else vehiclePosition

        _uiState.copyUpdate { copy(followingVehicle = new) }
    }

    private fun startTrackingVehicles() {
        viewModelScope.launch {
            startVehicleTrackingUseCase()
        }
        viewModelScope.launch {
            listenVehiclePositionUseCase().collect { result ->
                _uiState.copyUpdate { copy(trackedVehicles = result) }
                checkForFollowingVehicle(result)
            }
        }
    }

    private fun saveJourneyToHome() {
        viewModelScope.launch {
            saveCurrentJourneyToHomeUseCase()
        }
    }

    private fun checkForFollowingVehicle(result: List<VehiclePosition>) {
        _uiState.value.followingVehicle?.let {
            result.forEach { vehicle ->
                if (vehicle.id == _uiState.value.followingVehicle?.id) {
                    _uiState.copyUpdate { copy(followingVehicle = vehicle) }
                    return@let
                }
            }
            _uiState.copyUpdate { copy(followingVehicle = null) }
        }
    }

    private fun setLocationAccessResult(hasAccess: Boolean) {
        _uiState.copyUpdate { copy(hasLocationAccess = hasAccess) }
    }

    companion object {
        private const val TAG = "JourneyDetailsViewModel"
    }
}
