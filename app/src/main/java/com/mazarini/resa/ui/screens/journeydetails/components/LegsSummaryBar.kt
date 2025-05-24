package com.mazarini.resa.ui.screens.journeydetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mazarini.resa.domain.model.journey.Leg
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.screens.journeyselection.component.LegItem
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme

@Composable
fun LegsSummaryBar(
    modifier: Modifier,
    legs: List<Leg>,
) {
    Column(
        modifier = modifier.background(color = MTheme.colors.background)
            .testTag("LegsSummaryBar")
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MTheme.colors.graph.minimal,
            thickness = 1.dp,
        )
        Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
            LazyRow(
                modifier = Modifier
                    .align(CenterVertically)
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                itemsIndexed(legs) { index, item ->
                    LegItem(
                        leg = item,
                        hasLeadingArrow = index != 0
                    )
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MTheme.colors.graph.minimal,
            thickness = 1.dp,
        )
    }
}

@Composable
@Preview
fun TripDetailsTopBarPreview() {
    ResaTheme(darkTheme = false) {
        LegsSummaryBar(
            modifier = Modifier.background(color = Color.White),
            legs = FakeFactory.legList(),
        )
    }
}