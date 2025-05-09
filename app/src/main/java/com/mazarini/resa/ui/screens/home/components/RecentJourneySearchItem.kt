package com.mazarini.resa.ui.screens.home.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.Mdivider
import com.mazarini.resa.ui.model.JourneySearch
import com.mazarini.resa.ui.screens.home.state.HomeUiEvent
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun RecentJourneySearchItem(
    modifier: Modifier = Modifier,
    journeySearch: JourneySearch,
    onEvent: (HomeUiEvent) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("RecentJourneySearchItem_${journeySearch.id}")
            .clickable {
                onEvent(HomeUiEvent.OnSavedJourneyClicked(journeySearch))
            },
    ) {
        Row {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(IntrinsicSize.Min)
                    .padding(vertical = 8.dp)
                    .padding(start = 24.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 4.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.End),
                        text = stringResource(id = R.string.from),
                        style = MTheme.type.fadedTextS,
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.End),
                        text = stringResource(id = R.string.to),
                        style = MTheme.type.fadedTextS,
                        textAlign = TextAlign.End,
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(top = 4.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f),
                        text = getOriginName(journeySearch),
                        overflow = TextOverflow.Ellipsis,
                        style = MTheme.type.textField.copy(fontSize = 14.sp),
                        maxLines = 3,
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f),
                        text = getDestName(journeySearch),
                        overflow = TextOverflow.Ellipsis,
                        style = MTheme.type.textField.copy(fontSize = 14.sp),
                        maxLines = 3,
                    )
                }
            }

            if (journeySearch.isLoading) {
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(32.dp)
                        .align(Alignment.CenterVertically),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MTheme.colors.primary,
                        trackColor = MTheme.colors.background,
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .align(Alignment.Top)
                        .testTag("DeleteRecentJourneySearchItem_${journeySearch.id}")
                        .clickable { onEvent(HomeUiEvent.DeleteRecentJourney(journeySearch.id)) },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp),
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = null,
                        tint = MTheme.colors.textSecondary,
                    )
                }
            }
        }
        Mdivider()
    }
}

@Previews
@Composable
fun RecentJourneySearchItemPreview() {
    ResaTheme {
        RecentJourneySearchItem(
            modifier = Modifier.background(MTheme.colors.background),
            journeySearch = FakeFactory.journeySearch(),
            onEvent = { }
        )
    }
}