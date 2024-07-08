package com.mazarini.resa.global.extensions

import androidx.compose.ui.graphics.Color

fun String?.asColor(): Color = Color(android.graphics.Color.parseColor(this))
