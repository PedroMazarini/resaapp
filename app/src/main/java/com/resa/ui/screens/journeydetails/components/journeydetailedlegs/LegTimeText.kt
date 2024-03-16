package com.resa.ui.screens.journeydetails.components.journeydetailedlegs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resa.domain.model.journey.JourneyTimes
import com.resa.domain.model.journey.Leg
import com.resa.global.extensions.time_HH_mm
import com.resa.global.fake.FakeFactory
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import java.util.Date

@Composable
fun LegTimeText(
    modifier: Modifier,
    legTime: JourneyTimes
) {
    Column(modifier = modifier) {
        when (legTime) {
            is JourneyTimes.Changed -> LegChangedTime(time = legTime)
            is JourneyTimes.Planned -> LegPlannedTime(time = legTime)
        }
    }
}

@Composable
private fun LegChangedTime(time: JourneyTimes.Changed) {
    Text(
        text = time.estimated.time_HH_mm(),
        style = MTheme.type.hoursStyle,
    )
    Text(
        text = time.planned.time_HH_mm(),
        style = MTheme.type.hoursStyle.copy(
            textDecoration = TextDecoration.LineThrough,
        )
    )
}

@Composable
private fun LegPlannedTime(time: JourneyTimes.Planned) {
    Text(
        text = time.time.time_HH_mm(),
        style = MTheme.type.hoursStyle,
    )
}

@Composable
@Preview
fun LegTimeTextPreview() {
    ResaTheme {
        LegTimeText(
            modifier = Modifier,
            legTime = JourneyTimes.Changed(
                estimated = Date(),
                planned = Date(),
                isLiveTracking = true,
            )
        )
    }
}