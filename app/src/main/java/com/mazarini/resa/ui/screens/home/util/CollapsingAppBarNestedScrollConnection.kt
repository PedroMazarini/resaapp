package com.mazarini.resa.ui.screens.home.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

internal class CollapsingAppBarNestedScrollConnection(
    val headerMaxHeight: Float
) : NestedScrollConnection {

   var headerOffset: Float by mutableFloatStateOf(0f)
       private set

   val shouldScroll = mutableStateOf(true)
   override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
       val delta = available.y
       val newOffset = headerOffset + delta
       val previousOffset = headerOffset
       if (shouldScroll.value) headerOffset = newOffset.coerceIn(-headerMaxHeight, 0f)
       val consumed = headerOffset - previousOffset
       return Offset(0f, consumed.toFloat())
   }
}