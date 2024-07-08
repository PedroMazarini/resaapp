package com.mazarini.resa.ui.screens.journeydetails.components.journeydetailedlegs

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.mazarini.resa.domain.model.journey.JourneyTimes
import com.mazarini.resa.global.extensions.time_HH_mm
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
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