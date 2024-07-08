package com.mazarini.resa.ui.commoncomponents.journeySearchFilters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.asCardBackground
import com.mazarini.resa.global.extensions.asCardElevation
import com.mazarini.resa.global.extensions.stringRes
import com.mazarini.resa.ui.screens.locationsearch.model.JourneyFilters
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun JourneyFilterTab(
    modifier: Modifier,
    filters: JourneyFilters,
    onReferenceChanged: (isDepart: Boolean) -> Unit,
) {

    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = 0.dp.asCardElevation(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 24.dp),
            colors = MTheme.colors.btnBackground.asCardBackground(),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = 0.dp.asCardElevation(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp)
                        .padding(start = 4.dp)
                        .clickable { onReferenceChanged(true) },
                    colors = getTabBackground(filters.isDepartureFilters),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 14.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = R.string.depart_at.stringRes(),
                        style = getTabTextStyle(filters.isDepartureFilters),
                    )
                }
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = 0.dp.asCardElevation(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp)
                        .padding(end = 4.dp)
                        .align(Alignment.CenterVertically)
                        .clickable { onReferenceChanged(false) },
                    colors = getTabBackground(filters.isDepartureFilters.not()),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 14.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = R.string.arrive_by.stringRes(),
                        style = getTabTextStyle(filters.isDepartureFilters),
                    )
                }
            }
        }
    }
}

@Composable
fun getTabBackground(isHighlighted: Boolean) =
    if (isHighlighted) MTheme.colors.background.asCardBackground()
    else MTheme.colors.btnBackground.copy(alpha = 0f).asCardBackground()

@Composable
fun getTabTextStyle(isHighlighted: Boolean) =
    if (isHighlighted) {
        MTheme.type.highlightTextS
            .copy(
                fontWeight = FontWeight.SemiBold,
                color = MTheme.colors.textSecondary,
            )
    } else MTheme.type.fadedTextS

@Composable
@Previews
fun JourneyFilterTabPreview() {
    ResaTheme {
        Column(modifier = Modifier.background(Color.White)) {
            JourneyFilterTab(
                modifier = Modifier,
                filters = JourneyFilters(),
                onReferenceChanged = {},
            )
        }
    }
}