package com.mazarini.resa.ui.screens.journeydetails.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.journey.Warning
import com.mazarini.resa.domain.model.journey.WarningSeverity
import com.mazarini.resa.domain.model.journey.filteredMessage
import com.mazarini.resa.global.extensions.asCardBackground
import com.mazarini.resa.global.extensions.asCardElevation
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.translateWarnings

@Composable
fun WarningBar(
    modifier: Modifier = Modifier,
    warnings: List<Warning>,
    isExpandedInitial: Boolean = false,
) {
    val isExpanded = remember { mutableStateOf(isExpandedInitial) }
    val displayWarnings = remember { mutableStateOf(warnings) }
    val highestSeverity = warnings.maxOfOrNull { it.severity } ?: WarningSeverity.LOW
    val isTranslated = remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(4.dp),
        elevation = 2.dp.asCardElevation(),
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                isExpanded.value = !isExpanded.value
            },
        colors = highestSeverity.bgColor().asCardBackground(),
        border = BorderStroke(1.dp, highestSeverity.color()),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                ) {
                    if (isExpanded.value) {
                        displayWarnings.value.forEach { warning ->
                            WarningMessage(warning)
                        }
                    } else {
                        WarningMessage(displayWarnings.value.first(), maxLines = 1)
                    }
                }
                val iconAlignment =
                    if (isExpanded.value) Alignment.Top else Alignment.CenterVertically
                Icon(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(14.dp)
                        .align(iconAlignment)
                        .rotate(if (isExpanded.value) 180f else 0f),
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    tint = MTheme.colors.textPrimary,
                    contentDescription = null,
                )
            }
            if (isExpanded.value) {
                val clipboardManager: ClipboardManager = LocalClipboardManager.current
                val context = LocalContext.current
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Box (
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterVertically)
                            .clickable {
                                copyWarningMessages(clipboardManager, displayWarnings.value)
                            }
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .size(20.dp),
                            painter = painterResource(id = R.drawable.ic_copy),
                            contentDescription = null,
                            tint = MTheme.colors.textPrimary,
                        )
                    }
                    TransLateButton(warnings = warnings, isTranslated = isTranslated) { warnings ->
                        displayWarnings.value = warnings
                    }
                }
            }
        }
    }
}

fun copyWarningMessages(clipboardManager: ClipboardManager, warnings: List<Warning>) {
    clipboardManager.setText(
        AnnotatedString(
            warnings.joinToString("\n") { it.filteredMessage }
        )
    )
}

@Composable
fun RowScope.TransLateButton(
    warnings: List<Warning>,
    isTranslated: MutableState<Boolean>,
    result: (List<Warning>) -> Unit,
) {
    val icon = painterResource(
        id =
        if (isTranslated.value) R.drawable.ic_return
        else R.drawable.ic_translate
    )
    val text = stringResource(
        id = if (isTranslated.value) R.string.see_original
        else R.string.translate
    )
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .clickable {
                if (isTranslated.value) {
                    result(warnings)
                    isTranslated.value = !isTranslated.value
                } else {
                    translateWarnings(context, coroutine, warnings) { translatedWarnings ->
                        result(translatedWarnings)
                        isTranslated.value = !isTranslated.value
                    }
                }
            },
    ) {
        if (isTranslated.value) {
            Icon(
                modifier = Modifier
                    .size(14.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_return),
                contentDescription = null,
                tint = MTheme.colors.textPrimary,
            )
        } else {
            Image(
                modifier = Modifier
                    .size(14.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_translate),
                contentDescription = null,
            )
        }
        Text(
            modifier = Modifier.padding(8.dp),
            text = text,
            style = MTheme.type.hoursStyle,
        )
    }
}

@Composable
private fun WarningMessage(
    warning: Warning,
    maxLines: Int = Int.MAX_VALUE,
) {
    val myId = "inlineContent"
    val text = buildAnnotatedString {
        appendInlineContent(myId, "[icon]")
        append(warning.filteredMessage)
    }

    val inlineContent = mapOf(
        Pair(myId,
            InlineTextContent(
                Placeholder(
                    width = 18.sp,
                    height = 14.sp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline
                )
            ) {
                Icon(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(14.dp),
                    painter = painterResource(id = R.drawable.ic_alert),
                    tint = warning.severity.color(),
                    contentDescription = null,
                )
            }
        )
    )
    Text(
        modifier = Modifier
            .padding(end = 24.dp),
        text = text,
        inlineContent = inlineContent,
        style = MTheme.type.secondaryText,
        overflow = TextOverflow.Ellipsis,
        maxLines = maxLines,
    )
}

@Composable
@Preview
fun WarningBarDarkPreview() {
    ResaTheme(darkTheme = true) {
        WarningBar(
            warnings = listOf(
                Warning(
                    message = "This is a warning with a very long message that should wrap to the next line and be truncated with an ellipsis",
                    severity = WarningSeverity.HIGH
                ),
                Warning(
                    message = "This is a warning",
                    severity = WarningSeverity.LOW
                ),
                Warning(
                    message = "This is a warning",
                    severity = WarningSeverity.MEDIUM
                ),
            ),
        )
    }
}

@Composable
@Preview
fun WarningBarDarkLowPreview() {
    ResaTheme(darkTheme = true) {
        WarningBar(
            warnings = listOf(
                Warning(
                    message = "This is a warning",
                    severity = WarningSeverity.LOW
                ),
                Warning(
                    message = "This is a warning",
                    severity = WarningSeverity.LOW
                ),
            ),
        )
    }
}

@Composable
@Preview
fun WarningBarOpenMediumDarkPreview() {
    ResaTheme(darkTheme = true) {
        WarningBar(
            warnings = listOf(
                Warning(
                    message = "This is a warning",
                    severity = WarningSeverity.LOW
                ),
                Warning(
                    message = "This is a warning",
                    severity = WarningSeverity.MEDIUM
                ),
            ),
            isExpandedInitial = true,
        )
    }
}

@Composable
@Preview
fun WarningBarOpenDarkPreview() {
    ResaTheme(darkTheme = true) {
        WarningBar(
            warnings = listOf(
                Warning(
                    message = "This is a warning with a very long message that should wrap to the next line and be truncated with an ellipsis",
                    severity = WarningSeverity.HIGH
                ),
                Warning(
                    message = "This is a warning",
                    severity = WarningSeverity.LOW
                ),
                Warning(
                    message = "This is a warning",
                    severity = WarningSeverity.MEDIUM
                ),
            ),
            isExpandedInitial = true,
        )
    }
}
@Composable
@Preview
fun WarningBarPreview() {
    ResaTheme(darkTheme = false) {
        WarningBar(
            modifier = Modifier.background(color = Color.White),
            warnings = listOf(
                Warning(
                    message = "This is a warning with a very long message that should wrap to the next line and be truncated with an ellipsis",
                    severity = WarningSeverity.HIGH
                ),
                Warning(
                    message = "This is a warning",
                    severity = WarningSeverity.LOW
                ),
                Warning(
                    message = "This is a warning",
                    severity = WarningSeverity.MEDIUM
                ),
            ),
        )
    }
}

@Composable
@Preview
fun WarningBarOpenMediumPreview() {
    ResaTheme(darkTheme = false) {
        WarningBar(
            modifier = Modifier.background(color = Color.White),
            warnings = listOf(
                Warning(
                    message = "This is a warning",
                    severity = WarningSeverity.LOW
                ),
                Warning(
                    message = "This is a warning",
                    severity = WarningSeverity.MEDIUM
                ),
            ),
            isExpandedInitial = true,
        )
    }
}

@Composable
@Preview
fun WarningBarOpenPreview() {
    ResaTheme(darkTheme = false) {
        WarningBar(
            modifier = Modifier.background(color = Color.White),
            warnings = listOf(
                Warning(
                    message = "This is a warning with a very long message that should wrap to the next line and be truncated with an ellipsis",
                    severity = WarningSeverity.HIGH
                ),
                Warning(
                    message = "This is a warning",
                    severity = WarningSeverity.LOW
                ),
                Warning(
                    message = "This is a warning",
                    severity = WarningSeverity.MEDIUM
                ),
            ),
            isExpandedInitial = true,
        )
    }
}
