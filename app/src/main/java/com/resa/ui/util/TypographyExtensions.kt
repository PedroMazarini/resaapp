package com.resa.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import com.resa.ui.theme.MTheme

fun TextStyle.strikeThrough(): TextStyle =
    this.copy(textDecoration = TextDecoration.LineThrough)

@Composable
fun TextStyle.asAlert(): TextStyle =
    this.copy(color = MTheme.colors.alert)

fun TextStyle.fontSize(size: TextUnit): TextStyle =
    this.copy(fontSize = size)

fun TextStyle.color(color: Color): TextStyle =
    this.copy(color = color)
