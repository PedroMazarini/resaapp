package com.resa.ui.screens.journeyselection.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.resa.domain.usecases.QueryJourneysUseCase
import com.resa.global.logd
import com.resa.ui.screens.journeyselection.state.JourneySelectionUiEvent.PlaceHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JourneySelectionViewModel
@Inject
constructor(
    private val queryJourneysUseCase: QueryJourneysUseCase,
) : ViewModel() {

    val uiState: JourneySelectionUiState = JourneySelectionUiState()

    init {
        viewModelScope.launch {
            queryJourneys()
        }
    }

    private suspend fun queryJourneys() {
        uiState.journeysResult.value = queryJourneysUseCase()
    }

    fun onEvent(event: JourneySelectionUiEvent) {
        logd(className = TAG, "onEvent: ")
        when (event) {
            PlaceHolder -> {}
            is JourneySelectionUiEvent.DateFilterChanged -> TODO()
            is JourneySelectionUiEvent.IsDepartFilterChanged -> TODO()
            is JourneySelectionUiEvent.TimeFilterChanged -> TODO()
        }
    }

    private companion object {
        const val TAG = "JourneySelectionViewModel"
    }
}
