package com.resa.ui.screens.journeyselection.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.resa.global.fake.FakeFactory
import com.resa.domain.model.journey.Journey
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.Previews

@Composable
fun JourneyItem(
    modifier: Modifier = Modifier,
    journey: Journey,
    onJourneyClicked: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .clickable { onJourneyClicked(journey.id) },
    ) {
        JourneyItemHeader(
            modifier = Modifier.padding(horizontal = 24.dp),
            journey = journey,
        )
        JourneyTimeDetail(
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 24.dp),
            journey = journey,
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 24.dp),
            color = MTheme.colors.separatorLight,
            thickness = 1.dp,
        )
    }
}

@Composable
@Previews
fun TSearchItemPreview() {
    ResaTheme {
        JourneyItem(
            modifier = Modifier.background(Color.White),
            journey = FakeFactory.journey(),
            onJourneyClicked = {},
        )
    }
}