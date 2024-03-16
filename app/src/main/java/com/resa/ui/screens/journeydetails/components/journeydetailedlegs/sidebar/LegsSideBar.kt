package com.resa.ui.screens.journeydetails.components.journeydetailedlegs.sidebar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resa.domain.model.TransportMode
import com.resa.domain.model.journey.Leg
import com.resa.global.fake.FakeFactory
import com.resa.ui.theme.ResaTheme

@Composable
fun LegsSideBar(
    modifier: Modifier = Modifier,
    leg: Leg,
) {
    Box(
        modifier = modifier.width(24.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        when (leg) {
            is Leg.Transport -> TransportBar(leg)
            else -> WalkLink(leg)
        }
    }
}

@Composable
@Preview
fun LegsSideBarPreview() {
    ResaTheme {
        LegsSideBar(
            leg = FakeFactory.leg(mode = TransportMode.ferry)
        )
    }
}