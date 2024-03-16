package com.resa.ui.screens.journeydetails.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resa.domain.usecases.journey.FetchSelectedJourneyDetailsUseCase
import com.resa.domain.usecases.journey.GetSelectedJourneyUseCase
import com.resa.global.loge
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JourneyDetailsViewModel
@Inject
constructor(
    private val getSelectedJourneyUseCase: GetSelectedJourneyUseCase,
    private val fetchSelectedJourneyDetailsUseCase: FetchSelectedJourneyDetailsUseCase,
) : ViewModel() {

    val uiState: JourneyDetailsUiState = JourneyDetailsUiState()

    init {
        getSelectedJourney()
    }

    private fun getSelectedJourney() {
        viewModelScope.launch {
            fetchSelectedJourneyDetailsUseCase()
            getSelectedJourneyUseCase().collect { result ->
                result?.let {
                    loge("JourneyDetailsViewModel: getSelectedJourney: $it")
                    uiState.selectedJourney.value = it
                }
            }
        }
    }

    fun onEvent(event: JourneyDetailsUiEvent) {
        when (event) {
            else -> {}
        }
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
