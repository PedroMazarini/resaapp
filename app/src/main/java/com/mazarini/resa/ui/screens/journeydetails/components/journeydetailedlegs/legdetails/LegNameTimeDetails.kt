package com.mazarini.resa.ui.screens.journeydetails.components.journeydetailedlegs.legdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.journey.JourneyTimes
import com.mazarini.resa.domain.model.journey.Warning
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.screens.journeydetails.components.WarningBar
import com.mazarini.resa.ui.screens.journeydetails.components.journeydetailedlegs.LegTimeText
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import java.util.Date

@Composable
fun LegNameTimeDetails(
    modifier: Modifier = Modifier,
    name: String,
    platform: String = "",
    legTime: JourneyTimes,
    warnings: List<Warning> = emptyList(),
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 24.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = name,
                    style = MTheme.type.secondaryText.copy(fontSize = 14.sp),
                    overflow = TextOverflow.Visible,
                    maxLines = 4,
                )
                if (platform.isNotEmpty()) {
                    Text(
                        modifier = Modifier
                            .padding(top = 4.dp),
                        text = platformStyling(platform = platform),
                        style = MTheme.type.secondaryLightText.copy(fontSize = 14.sp),
                        overflow = TextOverflow.Visible,
                        maxLines = 4,
                    )
                }
                if (warnings.isNotEmpty()) {
                    WarningBar(
                        modifier = Modifier.padding(end = 8.dp, top = 8.dp),
                        warnings = warnings,
                    )
                }
            }
            LegTimeText(
                modifier = Modifier.align(Alignment.CenterVertically),
                legTime = legTime,
            )
        }
    }
}

@Composable
private fun platformStyling(platform: String): AnnotatedString {
    return buildAnnotatedString {
        withStyle(MTheme.type.secondaryLightText.toSpanStyle()) {
            append(stringResource(id = R.string.platform_name, ""))
        }
        append(" ")
        withStyle(
            MTheme.type.secondaryLightText.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
        ).toSpanStyle()) {
            append(platform)
        }
    }
}

@Composable
@Preview
fun ArrivalWalkLegDetailsPreview() {
    ResaTheme {
        LegNameTimeDetails(
            modifier = Modifier.background(MTheme.colors.background),
            name = "Smörkärnegatan",
            legTime = JourneyTimes.Changed(
                estimated = Date(),
                planned = Date(),
                isLiveTracking = true,
            ),
            warnings = FakeFactory.highWarnings(),
            platform = "B",
        )
    }
}