package com.mazarini.resa.ui.util

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned

@Stable
fun Modifier.onHeightChanged(
    onHeight: (height: Int) -> Unit
) = this.onGloballyPositioned {
    onHeight(it.size.height)
}