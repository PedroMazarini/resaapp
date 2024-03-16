package com.resa.ui.screens.journeydetails.components.journeydetailedlegs.legdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resa.domain.model.journey.JourneyTimes
import com.resa.domain.model.journey.Warning
import com.resa.global.fake.FakeFactory
import com.resa.ui.screens.journeydetails.components.WarningBar
import com.resa.ui.screens.journeydetails.components.journeydetailedlegs.LegTimeText
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import java.util.Date

@Composable
fun LegNameTimeDetails(
    modifier: Modifier = Modifier,
    name: String,
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
        )
    }
}