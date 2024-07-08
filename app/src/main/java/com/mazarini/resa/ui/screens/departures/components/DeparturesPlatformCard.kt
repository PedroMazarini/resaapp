package com.mazarini.resa.ui.screens.departures.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.stoparea.StopJourney
import com.mazarini.resa.global.extensions.asCardElevation
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews
import com.mazarini.resa.ui.util.TimeUpdateInterval
import com.mazarini.resa.ui.util.color
import com.mazarini.resa.ui.util.diffFrom
import com.mazarini.resa.ui.util.fontSize
import com.mazarini.resa.ui.util.getTimeUpdate

@Composable
fun DeparturesPlatformCard(
    platformJourneys: List<StopJourney>,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp.asCardElevation(),
        colors = CardDefaults.cardColors(
            containerColor = MTheme.colors.surface,
        )
    ) {
        TableHeader(
            platform = platformJourneys.first().platform,
        )
        platformJourneys.forEachIndexed { index, stopJourney ->
            val now by getTimeUpdate(interval = TimeUpdateInterval.TEN_SECONDS)
            val departInMilli = remember { mutableLongStateOf(stopJourney.time.diffFrom(now)) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LineBox(stopJourney = stopJourney)
                TextItem(text = stopJourney.direction)
                NextIn(departInMilli.longValue)
            }
            if (index != platformJourneys.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = MTheme.colors.primary,
                    thickness = 1.dp,
                )
            }
        }
    }
}

@Composable
fun TableHeader(
    modifier: Modifier = Modifier,
    platform: String,
) {
    Column(
        modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp),
            text = stringResource(id = R.string.platform) + " $platform",
            style = MTheme.type.secondaryText
                .fontSize(14.sp)
                .color(MTheme.colors.textPrimary),
        )
        Row(
            modifier
                .background(color = MTheme.colors.primary)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(vertical = 4.dp),
                text = stringResource(id = R.string.line).lowercase(),
                style = MTheme.type.secondaryText
                    .fontSize(12.sp)
                    .color(Color.White),
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                text = stringResource(id = R.string.destination).lowercase(),
                style = MTheme.type.secondaryText
                    .fontSize(12.sp)
                    .color(Color.White),
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(vertical = 4.dp),
                text = stringResource(id = R.string.next_in).lowercase(),
                style = MTheme.type.secondaryText
                    .fontSize(12.sp)
                    .color(Color.White),
            )
        }
    }
}

@Composable
@Previews
fun DeparturesPlatformCardPreview() {
    val list = FakeFactory.stopJourneyList()
    ResaTheme {
        DeparturesPlatformCard(
            platformJourneys = list,
        )
    }
}