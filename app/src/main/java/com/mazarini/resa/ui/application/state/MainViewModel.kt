package com.mazarini.resa.ui.application.state

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazarini.resa.global.model.ThemeSettings
import com.mazarini.resa.global.preferences.PrefsProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val prefsProvider: PrefsProvider,
): ViewModel() {

    val themeState = mutableStateOf(ThemeSettings.SYSTEM)
    init {
        observeThemeSettings()
    }

    private fun observeThemeSettings() {
        viewModelScope.launch {
            prefsProvider.collectThemeSettings().collect {
                themeState.value = it
            }
        }
    }
}
