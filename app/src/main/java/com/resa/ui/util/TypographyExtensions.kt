package com.resa.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.font.FontWeight.Companion.W800
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import com.resa.R
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

fun TextStyle.asSpanStyle(): SpanStyle =
    SpanStyle(
        color = this.color,
        background = this.background,
        fontWeight = this.fontWeight,
        fontStyle = this.fontStyle,
        textDecoration = this.textDecoration,
        shadow = this.shadow,
        fontSize = this.fontSize,
        fontFamily = this.fontFamily,
        letterSpacing = this.letterSpacing,
        baselineShift = this.baselineShift,
        textGeometricTransform = this.textGeometricTransform,
    )

fun SpanStyle.asTextStyle(): TextStyle =
    TextStyle(
        color = this.color,
        background = this.background,
        fontWeight = this.fontWeight,
        fontStyle = this.fontStyle,
        textDecoration = this.textDecoration,
        shadow = this.shadow,
        fontSize = this.fontSize,
        fontFamily = this.fontFamily,
        letterSpacing = this.letterSpacing,
        baselineShift = this.baselineShift,
        textGeometricTransform = this.textGeometricTransform,
    )

@Composable
fun String.firstBold(style: SpanStyle) =
    AnnotatedString(
        text = this.first().toString(),
        spanStyle = style.copy(fontWeight = W800),
    ).plus(
        AnnotatedString(
            text = this.substring(1),
            spanStyle = style,
        )
    )
