package com.mazarini.resa.ui.screens.journeydetails.components.journeydetailedlegs.sidebar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.TransportMode
import com.mazarini.resa.domain.model.journey.LegColors
import com.mazarini.resa.domain.model.journey.LegDetails
import com.mazarini.resa.global.extensions.asCardBackground
import com.mazarini.resa.global.extensions.asCardElevation
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.iconResource

@Composable
fun BoxScope.TransportBar(
    details: LegDetails,
    transportMode: TransportMode,
    colors: LegColors,
) {

    val barBottomPadding = if (details.isLastLeg) 12.dp else 0.dp

    Box(
        modifier = Modifier
            .width(12.dp)
            .padding(bottom = barBottomPadding)
            .fillMaxHeight()
            .background(
                color = colors.primaryColor(),
                shape = RoundedCornerShape(12.dp)
            )
    )
    if (colors.hasBorder()) {
        Box(
            modifier = Modifier
                .width(8.dp)
                .fillMaxSize()
                .padding(bottom = 4.dp)
                .background(
                    color = colors.secondaryColor(),
                    shape = RoundedCornerShape(12.dp)
                )
        )
    }
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
            painter = painterResource(id = transportMode.iconResource()),
            contentDescription = null,
            tint = MTheme.colors.surfaceBlur,
        )
    }
    if (details.isLastLeg) {
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
fun TransportBarDarkPreview() {
    ResaTheme(darkTheme = true) {
        LegsSideBar(
            leg = FakeFactory.leg(
                mode = TransportMode.ferry,
            )
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
            )
        )
    }
}