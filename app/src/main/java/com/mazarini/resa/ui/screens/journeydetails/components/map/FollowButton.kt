package com.mazarini.resa.ui.screens.journeydetails.components.map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.VehiclePosition
import com.mazarini.resa.global.extensions.asCardBackground
import com.mazarini.resa.global.extensions.asCardElevation
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.commoncomponents.LegBoxIcon
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.Mdivider
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiEvent
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme

@Composable
fun FollowButton(
    modifier: Modifier = Modifier,
    vehicle: VehiclePosition,
    isFollowing: Boolean,
    onEvent: (JourneyDetailsUiEvent) -> Unit = {},
) {
    val (border, followText, followTextColor) = getConfig(isFollowing)

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 16.dp.asCardElevation(),
        modifier = modifier
            .width(86.dp)
            .clickable {
                onEvent(JourneyDetailsUiEvent.OnFollowClicked(vehicle))
            },
        colors = MTheme.colors.surface.asCardBackground(),
        border = BorderStroke(border, MTheme.colors.primary),
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.CenterHorizontally),
        ) {
            LegBoxIcon(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                legName = vehicle.name,
                legColors = vehicle.colors,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .align(Alignment.CenterHorizontally),
                text = vehicle.directionName,
                style = MTheme.type.secondaryText.copy(textAlign = TextAlign.Center),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Mdivider(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.CenterHorizontally)
                    .width(12.dp),
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .align(Alignment.CenterHorizontally),
                text = followText,
                style = MTheme.type.secondaryLightText
                    .copy(textAlign = TextAlign.Center, color = followTextColor),
            )
        }
    }
}

@Composable
fun getConfig(following: Boolean): Triple<Dp, String, Color> {
    return if (following) {
        Triple(2.dp, stringResource(id = R.string.following), MTheme.colors.primary)
    } else {
        Triple(0.dp, stringResource(id = R.string.follow), MTheme.colors.graph.disabled)
    }
}

@Composable
@Preview
fun FollowButtonPreview() {
    ResaTheme {
        FollowButton(vehicle = FakeFactory.busTracking(), isFollowing = false)
    }
}

@Composable
@Preview
fun FollowButtonDarkPreview() {
    ResaTheme(darkTheme = true) {
        FollowButton(vehicle = FakeFactory.busTracking(), isFollowing = true)
    }
}