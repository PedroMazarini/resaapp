package com.mazarini.resa.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.commoncomponents.Animation
import com.mazarini.resa.ui.commoncomponents.LottieAnim
import com.mazarini.resa.ui.navigation.Route
import com.mazarini.resa.ui.screens.home.state.HomeUiEvent
import com.mazarini.resa.ui.screens.journeyselection.component.formatForArrivalTime
import com.mazarini.resa.ui.screens.journeyselection.component.getDepartText
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews
import com.mazarini.resa.ui.util.TimeUpdateInterval
import com.mazarini.resa.ui.util.getTimeUpdate

@Composable
fun SavedHomeJourneyCard(
    modifier: Modifier = Modifier,
    journey: Journey,
    onEvent: (HomeUiEvent) -> Unit = {},
    navigateTo: (route: Route) -> Unit,
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                shape =  RoundedCornerShape(8.dp),
                width = 2.dp,
                color = MTheme.colors.lightDetail,
            )
            .clip(RoundedCornerShape(8.dp))
            .background(color = MTheme.colors.surface)
            .clickable {
                onEvent(HomeUiEvent.LoadSavedJourneyToHome)
                navigateTo(Route.JourneyDetails)
            },
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                PinHeader()
                JourneyDetails(journey = journey)
            }
            Box(
                modifier = Modifier
                    .align(Alignment.Top)
                    .clickable { onEvent(HomeUiEvent.DeleteSavedJourneyToHome) }
            ) {
                Icon(
                    modifier = Modifier.padding(8.dp),
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = null,
                    tint = MTheme.colors.textSecondary,
                )
            }
        }
    }
}

@Composable
fun JourneyDetails(journey: Journey) {
    val now = getTimeUpdate(interval = TimeUpdateInterval.TEN_SECONDS)

    Column {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(start = 8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 4.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    modifier = Modifier,
                    text = stringResource(id = R.string.from),
                    style = MTheme.type.fadedTextS,
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.End),
                    text = stringResource(id = R.string.to),
                    style = MTheme.type.fadedTextS,
                    textAlign = TextAlign.End,
                )
            }
            Column(
                modifier = Modifier.padding(top = 4.dp),
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    text = journey.originName.orEmpty(),
                    overflow = TextOverflow.Ellipsis,
                    style = MTheme.type.textField.copy(fontSize = 14.sp),
                    maxLines = 3,
                )
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    text = journey.destName.orEmpty(),
                    overflow = TextOverflow.Ellipsis,
                    style = MTheme.type.textField.copy(fontSize = 14.sp),
                    maxLines = 3,
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 8.dp),
                text = getDepartText(journey = journey, now = now.value) +" - ",
                overflow = TextOverflow.Ellipsis,
                style = MTheme.type.secondaryText.copy(fontSize = 14.sp),
                maxLines = 3,
            )
            Text(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .align(Alignment.CenterVertically),
                text = stringResource(id = R.string.arrive_at),
                style = MTheme.type.secondaryLightText,
            )
            Text(
                modifier = Modifier.padding(end = 4.dp),
                text = formatForArrivalTime(journey.arrivalTimes.arrival()),
                style = MTheme.type.secondaryText,
            )
        }
    }
}

@Composable
fun PinHeader() {
    Row {
        Icon(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(12.dp),
            painter = painterResource(id = R.drawable.ic_push_pin_filled),
            contentDescription = null,
            tint = MTheme.colors.textPrimary,
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = stringResource(R.string.following_this_journey),
            style = MTheme.type.secondaryLightText,
        )
        LottieAnim(
            modifier = Modifier
                .padding(start = 4.dp)
                .size(12.dp)
                .align(Alignment.CenterVertically),
            animation = Animation.LIVE,
        )
    }
}

@Composable
@Previews
fun SavedHomeJourneyCardPreview() {
    ResaTheme {
        SavedHomeJourneyCard(
            journey = FakeFactory.journey(),
            navigateTo = {},
        )
    }
}
