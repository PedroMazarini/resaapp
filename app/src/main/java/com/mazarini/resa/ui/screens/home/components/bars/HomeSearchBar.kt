package com.mazarini.resa.ui.screens.home.components.bars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun HomeSearchBar(
    modifier: Modifier = Modifier,
    onSearchBarClicked: () -> Unit = {},
    onTimeTableClicked: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
    ) {
        Card(
            modifier = Modifier
                .padding(start = 24.dp)
                .weight(1f)
                .fillMaxWidth()
                .testTag("HomeSearchBar")
                .clickable { onSearchBarClicked() },
            shape = RoundedCornerShape(
                topStart = 4.dp,
                bottomStart = 4.dp,
                topEnd = 0.dp,
                bottomEnd = 0.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MTheme.colors.surface,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier
                        .padding(start = 24.dp)
                        .padding(vertical = 18.dp)
                        .size(20.dp),
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    tint = MTheme.colors.textSecondary,
                )
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f),
                    text = stringResource(id = R.string.where_to_question),
                    maxLines = 1,
                    style = MTheme.type.fadedTextS,
                    fontSize = 16.sp,
                )
            }
        }

        Card(
            modifier = Modifier
                .padding(start = 4.dp, end = 24.dp)
                .testTag("HomeTimeTable")
                .clickable { onTimeTableClicked() }
                .fillMaxHeight(),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                bottomStart = 0.dp,
                topEnd = 4.dp,
                bottomEnd = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MTheme.colors.primary,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(4.dp)
                    .fillMaxSize()
            ) {
                Icon(
                    modifier = Modifier
                        .size(26.dp)
                        .align(Center),
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = null,
                    tint = MTheme.colors.graph.active,
                )
            }
        }
    }
}

@Composable
@Previews
fun HomeSearchBarPreview() {
    ResaTheme {
        HomeSearchBar {}
    }
}
