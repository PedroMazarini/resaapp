package com.resa.ui.screens.journeyqueryresult.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resa.global.loge
import com.resa.global.preferences.PrefsProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JourneyQueryResultViewModel
@Inject
constructor(
    private val prefsProvider: PrefsProvider,
) : ViewModel() {

    val uiState: JourneyQueryResultUiState = JourneyQueryResultUiState()

    init {
        loge("START START")
        viewModelScope.launch {
            loge("got string")
            uiState.test.value = prefsProvider.getCurrentJourneysQuery() ?: "Nao veio"
        }
    }
}
