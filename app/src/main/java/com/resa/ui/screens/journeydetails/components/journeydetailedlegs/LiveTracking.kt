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
fun LiveTracking(
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Text(text = "Live Tracking")
    }
}

@Composable
@Preview
fun LiveTrackingPreview() {
    ResaTheme {
        LiveTracking(
            modifier = Modifier,
        )
    }
}