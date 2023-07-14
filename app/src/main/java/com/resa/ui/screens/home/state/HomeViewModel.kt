package com.resa.ui.screens.home.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resa.domain.usecases.journey.GetSavedJourneySearchesUseCase
import com.resa.ui.screens.mapper.DomainToUiJourneySearchMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val getSavedJourneySearchesUseCase: GetSavedJourneySearchesUseCase,
    private val journeySearchMapper: DomainToUiJourneySearchMapper,
) : ViewModel() {

    val uiState: HomeUiState = HomeUiState()

    init {
        loadSavedJourneys()
    }

    private fun loadSavedJourneys() {
        viewModelScope.launch(Dispatchers.IO) {
            getSavedJourneySearchesUseCase()
                .flowOn(Dispatchers.Main)
                .collectLatest {
                    uiState.savedJourneys.value = it.map { domainJourneySearch ->
                        journeySearchMapper.map(domainJourneySearch)
                    }
                }
        }
    }
}
