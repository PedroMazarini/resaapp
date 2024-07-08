package com.mazarini.resa.ui.screens.journeyselection.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.TransportMode
import com.mazarini.resa.domain.model.journey.Leg
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.commoncomponents.LegBoxIcon
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews
import com.mazarini.resa.ui.util.iconResource

@Composable
fun LegItem(
    leg: Leg,
    hasLeadingArrow: Boolean,
) {
    Row(
        modifier = Modifier.background(color = MTheme.colors.background),
    ) {
        if (hasLeadingArrow) {
            Image(
                modifier = Modifier
                    .height(8.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(MTheme.colors.graph.disabled),
            )
        }
        Icon(
            modifier = Modifier.height(18.dp),
            painter = painterResource(id = leg.transportMode.iconResource()),
            contentDescription = null,
            tint = MTheme.colors.textSecondary,
        )

        when (leg.transportMode) {
            TransportMode.walk -> {
                Text(
                    modifier = Modifier.padding(top = 9.dp, start = 2.dp),
                    text = leg.durationInMinutes.toString() +"'",
                    style = MTheme.type.tertiaryLightText.copy(color = MTheme.colors.graph.disabled),
                )
            }
            else -> {
                LegBoxIcon(
                    modifier = Modifier.padding(start = 12.dp),
                    legName = leg.name,
                    legColors = leg.colors,
                )
            }
        }
    }
}

@Composable
@Previews
fun LegItemPreview() {
    ResaTheme {
        LegItem(
            leg = FakeFactory.leg(),
            hasLeadingArrow = true,
        )
    }
}