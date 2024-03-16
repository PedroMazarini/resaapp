package com.resa.ui.screens.journeydetails.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resa.R
import com.resa.domain.model.journey.Warning
import com.resa.domain.model.journey.WarningSeverity
import com.resa.global.extensions.asCardBackground
import com.resa.global.extensions.asCardElevation
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme

@Composable
fun WarningBar(
    modifier: Modifier = Modifier,
    warnings: List<Warning>,
    isExpandedInitial: Boolean = false,
) {
    val isExpanded = remember { mutableStateOf(isExpandedInitial) }
    val highestSeverity = warnings.maxOfOrNull { it.severity } ?: WarningSeverity.LOW

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
        Row (
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
        ) {
            Column (
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                if (isExpanded.value) {
                    warnings.forEach { warning ->
                        WarningMessage(warning)
                    }
                } else {
                    WarningMessage(warnings.first(), maxLines = 1)
                }
            }
            val iconAlignment = if (isExpanded.value) Alignment.Top else Alignment.CenterVertically
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
        append(warning.message)
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
