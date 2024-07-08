package com.mazarini.resa.ui.screens.journeyselection.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.TransportMode
import com.mazarini.resa.domain.model.journey.Leg
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.screens.journeyselection.state.JourneySelectionUiEvent
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.asPrimary
import com.mazarini.resa.ui.util.fontSize

@Composable
fun WalkOnlyJourney(
    modifier: Modifier,
    leg: Leg,
    onEvent: (JourneySelectionUiEvent) -> Unit,
) {

    val title = stringResource(
        id = R.string.walk_min_distance,
        leg.durationInMinutes,
        leg.distanceInMeters,
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                leg.direction?.let { direction ->
                    onEvent(JourneySelectionUiEvent.OnLegMapClicked(direction))
                }
            },
        verticalAlignment = CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 24.dp),
            text = title,
            style = MTheme.type.highlightTextS.fontSize(20.sp).asPrimary(),
        )
        Image(
            modifier = Modifier
                .padding(start = 12.dp)
                .size(24.dp)
                .align(CenterVertically),
            painter = painterResource(id = R.drawable.ic_maps),
            contentDescription = null,
        )
    }
}

@Composable
@Preview
fun WalkOnlyJourneyPreview() {
    ResaTheme(darkTheme = true) {
        WalkOnlyJourney(
            modifier = Modifier,
            leg = FakeFactory.leg(mode = TransportMode.walk),
            onEvent = { }
        )
    }
}