package com.mazarini.resa.ui.screens.journeydetails.components.journeydetailedlegs.legdetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.journey.Leg
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiEvent
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme

@Composable
fun WalkItem(
    modifier: Modifier = Modifier,
    leg: Leg,
    onEvent: (JourneyDetailsUiEvent) -> Unit = {},
) {

    Column {
        Row(
            modifier = modifier
                .padding(vertical = 16.dp)
                .padding(end = 24.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                text = stringResource(
                    id = R.string.walk_min_distance,
                    leg.durationInMinutes,
                    leg.distanceInMeters,
                ),
                style = MTheme.type.secondaryText.copy(fontSize = 14.sp),
                overflow = TextOverflow.Visible,
                maxLines = 1,
            )
            Image(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        leg.direction?.let { direction ->
                            onEvent(JourneyDetailsUiEvent.OnLegMapClicked(direction))
                        }
                    },
                painter = painterResource(id = R.drawable.ic_maps),
                contentDescription = null,
            )
        }
        LegsDivider()
    }
}


@Composable
private fun LegsDivider() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth(),
        color = MTheme.colors.graph.minimal,
        thickness = 1.dp,
    )
}

@Composable
@Preview
fun WalkItemPreview() {
    ResaTheme {
        WalkItem(
            modifier = Modifier.background(MTheme.colors.background),
            leg = FakeFactory.departWalkLeg(),
        )
    }
}