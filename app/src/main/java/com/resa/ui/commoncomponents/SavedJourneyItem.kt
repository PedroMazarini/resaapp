package com.resa.ui.commoncomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.resa.R
import com.resa.global.extensions.asCardElevation
import com.resa.global.fake.FakeFactory
import com.resa.ui.model.JourneySearch
import com.resa.ui.model.LocationType
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.Previews

@Composable
fun SavedJourneyItem(
    modifier: Modifier = Modifier,
    journeySearch: JourneySearch,
    showDeleteButton: Boolean = false,
    onItemClicked: (journeySearch: JourneySearch) -> Unit = {},
    onDelete: (id: Int) -> Unit = {},
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = 8.dp.asCardElevation(),
        modifier = modifier
            .width(200.dp)
            .padding(horizontal = 4.dp)
            .height(138.dp)
            .clickable { onItemClicked(journeySearch) },
    ) {
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.TopEnd,
        ) {

            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(color = MTheme.colors.background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(start = 24.dp)
                        .padding(vertical = 16.dp),
                ) {
                    Text(text = fromLocationString(
                            getOriginName(journeySearch),
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 3,
                    )
                    Text(text = toLocationString(
                        getDestName(journeySearch),
                    ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 3,
                    )
                }
                if (showDeleteButton) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clickable { onDelete(journeySearch.id) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = null,
                            tint = MTheme.colors.textSecondary,
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
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
