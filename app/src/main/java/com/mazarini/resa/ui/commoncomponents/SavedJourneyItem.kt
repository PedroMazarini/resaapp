package com.mazarini.resa.ui.commoncomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.asCardElevation
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.model.JourneySearch
import com.mazarini.resa.ui.model.LocationType
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun SavedJourneyItem(
    modifier: Modifier = Modifier,
    journeySearch: JourneySearch,
    showDeleteButton: Boolean = false,
    onItemClicked: (journeySearch: JourneySearch) -> Unit = {},
    onDelete: (id: String) -> Unit = {},
) {

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = 2.dp.asCardElevation(),
        modifier = modifier
            .padding(horizontal = 4.dp)
            .height(138.dp)
            .clickable {
                onItemClicked(journeySearch)
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .background(color = MTheme.colors.surface),
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    Text(
                        modifier = Modifier
                            .padding(start = 24.dp, top = 16.dp),
                        text = stringResource(id = R.string.from),
                        style = MTheme.type.fadedTextS,
                    )
                }
                Row(
                    modifier = Modifier
                ) {
                    FromIcon(
                        modifier = Modifier
                            .padding(top = 3.dp)
                            .padding(horizontal = 6.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(end = 8.dp),
                    ) {
                        Text(text =  getOriginName(journeySearch),
                            overflow = TextOverflow.Ellipsis,
                            style = MTheme.type.textField.copy(fontSize = 14.sp),
                            maxLines = 2,
                        )
                    }
                }
                Row(
                    modifier = Modifier
                ) {
                    ToIcon(
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(end = 8.dp),
                    ) {
                        Text(
                            modifier = Modifier,
                            text = stringResource(id = R.string.to),
                            style = MTheme.type.fadedTextS,
                        )
                        Text(text =  getDestName(journeySearch),
                            overflow = TextOverflow.Ellipsis,
                            style = MTheme.type.textField.copy(fontSize = 14.sp),
                            maxLines = 2,
                        )
                    }
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
            } else if (showDeleteButton) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Top)
                        .clickable { onDelete(journeySearch.id) },
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
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun FromIcon(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Icon(
            modifier = Modifier.size(12.dp),
            painter = painterResource(id = R.drawable.ic_trip_origin),
            contentDescription = null,
            tint = MTheme.colors.textSecondary,
        )
        DashedLine(
            Modifier
                .padding(top = 4.dp)
                .height(16.dp)
                .width(8.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun ToIcon(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(top = 0.dp),
    ) {
        DashedLine(
            Modifier
                .padding(bottom = 4.dp)
                .height(16.dp)
                .width(8.dp)
                .align(Alignment.CenterHorizontally)
        )
        Icon(
            modifier = Modifier
                .size(12.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.ic_pin),
            contentDescription = null,
            tint = MTheme.colors.textSecondary,
        )
    }
}

@Composable
fun getOriginName(journeySearch: JourneySearch): String {
    return if (journeySearch.origin.type == LocationType.gps)
        stringResource(id = R.string.my_location)
    else journeySearch.origin.name
}

@Composable
fun getDestName(journeySearch: JourneySearch): String {
    return if (journeySearch.destination.type == LocationType.gps)
        stringResource(id = R.string.my_location)
    else journeySearch.destination.name
}

@Composable
@Previews
fun SavedJourneyItemPreview() {
    ResaTheme {
        SavedJourneyItem(
            journeySearch = FakeFactory.journeySearch(),
            showDeleteButton = true,
        )
    }
}
