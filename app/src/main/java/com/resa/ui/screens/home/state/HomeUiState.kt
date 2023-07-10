package com.resa.ui.screens.home.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.util.*

data class HomeUiState(
    var placeHolder: MutableState<String> = mutableStateOf(""),
)