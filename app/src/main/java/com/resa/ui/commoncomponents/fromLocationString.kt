package com.resa.ui.commoncomponents

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import com.resa.R
import com.resa.ui.theme.MTheme

@Composable
fun fromLocationString(from: String) = AnnotatedString(
    text = stringResource(R.string.from) + "\r\n",
    spanStyle = MTheme.type.fadedText,
).plus(
    AnnotatedString(
        text = from + "\r\n",
        spanStyle = MTheme.type.highlightText
    )
)

@Composable
fun toLocationString(to: String) = AnnotatedString(
    text = stringResource(id = R.string.to) + "\r\n",
    spanStyle = MTheme.type.fadedText,
).plus(
    AnnotatedString(
        text = to,
        spanStyle = MTheme.type.highlightText,
    )
)
