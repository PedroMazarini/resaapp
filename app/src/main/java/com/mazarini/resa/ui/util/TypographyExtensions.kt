package com.mazarini.resa.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import com.mazarini.resa.ui.theme.MTheme

fun TextStyle.strikeThrough(): TextStyle =
    this.copy(textDecoration = TextDecoration.LineThrough)

@Composable
fun TextStyle.asAlert(): TextStyle =
    this.copy(color = MTheme.colors.alert)

@Composable
fun TextStyle.asPrimary(): TextStyle =
    this.copy(color = MTheme.colors.primary)

fun TextStyle.fontSize(size: TextUnit): TextStyle =
    this.copy(fontSize = size)

fun TextStyle.textAlign(align: TextAlign): TextStyle =
    this.copy(textAlign = align)

fun TextStyle.color(color: Color): TextStyle =
    this.copy(color = color)
