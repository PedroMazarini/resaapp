package com.resa.ui.screens.journeydetails.components.journeydetailedlegs.legdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resa.domain.model.journey.LegStop
import com.resa.R
import com.resa.global.fake.FakeFactory
import com.resa.global.loge
import com.resa.ui.commoncomponents.FadeInAnimation
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme

@Composable
fun LegStops(
    modifier: Modifier = Modifier,
    legTotalTime: String,
    legStops: List<LegStop>,
    drawerOpenStartState: Boolean = false,
) {

    var isDrawerOpen by remember { mutableStateOf(drawerOpenStartState) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { isDrawerOpen = isDrawerOpen.not() }
    ) {
        Row(
            modifier = Modifier
        ) {
            Icon(
                modifier = Modifier
                    .align(CenterVertically)
                    .rotate(if (isDrawerOpen) 180f else 0f),
                painter = painterResource(id = R.drawable.ic_arrow_down),
                tint = MTheme.colors.textSecondary,
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .padding(start = 12.dp, end = 8.dp),
                text = rideText(legStops.size, legTotalTime),
                style = MTheme.type.secondaryText,
                overflow = TextOverflow.Visible,
                maxLines = 1,
            )
        }
        if (isDrawerOpen) {
            FadeInAnimation {
                Column(modifier = Modifier.padding(start = 26.dp, end = 8.dp, top = 8.dp)) {
                    legStops.forEach {
                        LegStopItem(it)
                    }
                }
            }
        }
    }
}

@Composable
fun LegStopItem(legStop: LegStop) {
    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            modifier = Modifier,
            text = legStop.time,
            style = MTheme.type.secondaryText,
            overflow = TextOverflow.Visible,
            maxLines = 1,
        )
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = legStop.name,
            style = MTheme.type.secondaryLightText,
            overflow = TextOverflow.Visible,
            maxLines = 1,
        )
    }
}

@Composable
fun rideText(totalRides: Int, legTotalTime: String): String {
    return stringResource(id = R.string.ride_n_stops, totalRides, legTotalTime)
}

@Composable
@Preview
fun LegStopsPreview() {
    ResaTheme(darkTheme = false) {
        LegStops(
            modifier = Modifier.background(color = Color.White),
            legTotalTime = "4 min",
            legStops = FakeFactory.legStopList(),
        )
    }
}

@Composable
@Preview
fun LegStopsOpenPreview() {
    ResaTheme(darkTheme = false) {
        LegStops(
            modifier = Modifier.background(color = Color.White),
            legTotalTime = "4 min",
            legStops = FakeFactory.legStopList(73),
            drawerOpenStartState = true,
        )
    }
}
