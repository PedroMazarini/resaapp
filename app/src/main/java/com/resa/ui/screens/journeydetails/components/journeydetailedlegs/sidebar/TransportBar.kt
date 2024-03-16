package com.resa.ui.screens.journeydetails.components.journeydetailedlegs.sidebar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resa.R
import com.resa.domain.model.TransportMode
import com.resa.domain.model.journey.ArrivalLeg
import com.resa.domain.model.journey.JourneyTimes
import com.resa.domain.model.journey.Leg
import com.resa.global.extensions.asCardBackground
import com.resa.global.extensions.asCardElevation
import com.resa.global.fake.FakeFactory
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.iconResource
import java.util.Date

@Composable
fun BoxScope.TransportBar(leg: Leg.Transport) {

    val isArrival = leg.arrivalLeg is ArrivalLeg.Details
    val barBottomPadding = if (isArrival) 12.dp else 0.dp

    Box(
        modifier = Modifier
            .width(12.dp)
            .padding(bottom = barBottomPadding)
            .fillMaxHeight()
            .background(
                color = leg.colors.background,
                shape = RoundedCornerShape(12.dp)
            )
    )
    Card(
        modifier = Modifier.size(24.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp.asCardElevation(),
        colors = Color.White.asCardBackground(),
    ) {
        Icon(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = leg.transportMode.iconResource()),
            contentDescription = null,
            tint = MTheme.colors.textSecondary,
        )
    }
    if (isArrival) {
        Image(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(24.dp),
            painter = painterResource(id = R.drawable.ic_destination),
            contentDescription = null,
        )
    }
}

@Composable
@Preview
fun TransportBarPreview() {
    ResaTheme {
        LegsSideBar(
            leg = FakeFactory.leg(
                mode = TransportMode.ferry,
                arrivalLeg = ArrivalLeg.Details(
                    name = "Liseberg",
                    arrivalTime = JourneyTimes.Planned(
                        time = Date(),
                        isLiveTracking = true,
                    )
                )
            )
        )
    }
}