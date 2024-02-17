package com.resa.ui.screens.journeyselection.model

import androidx.compose.runtime.Composable

data class TabItem (
    val title: String,
    val screen: @Composable () -> Unit
)