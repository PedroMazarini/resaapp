package com.resa.ui.screens.journeyselection.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.map
import com.resa.domain.usecases.GetCurrentJourneyQueryUseCase
import com.resa.domain.usecases.QueryJourneysUseCase
import com.resa.domain.usecases.SaveCurrentJourneyQueryUseCase
import com.resa.global.logd
import com.resa.global.loge
import com.resa.ui.screens.journeyselection.state.JourneySelectionUiEvent.PlaceHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JourneySelectionViewModel
@Inject
constructor(
    private val queryJourneysUseCase: QueryJourneysUseCase,
    private val getCurrentJourneyQueryUseCase: GetCurrentJourneyQueryUseCase,
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
