package com.resa.global.extensions

import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.asCardElevation(): CardElevation {
    return CardDefaults.cardElevation(defaultElevation = this)
}

@Composable
fun Color.asCardBackground(): CardColors {
    return CardDefaults.cardColors(containerColor = this)
}
