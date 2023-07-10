package com.resa.ui.commoncomponents.journeySearchFilters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resa.R
import com.resa.global.extensions.stringRes
import com.resa.ui.screens.locationsearch.model.JourneyFilters
import com.resa.ui.screens.locationsearch.state.LocationSearchUiState
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import java.util.Date

@Composable
fun JourneyFilter(
    modifier: Modifier = Modifier,
    filters: JourneyFilters,
    onReferenceChanged: (isDepart: Boolean) -> Unit,
    onDateChanged: (Date) -> Unit,
    onTimeChanged: (Date) -> Unit,
    dismissFilters: () -> Unit,
) {

    Column(
        modifier
            .fillMaxWidth()
            .background(color = MTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Divider(
            modifier = Modifier
                .padding(top = 4.dp)
                .height(4.dp)
                .width(56.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MTheme.colors.separatorLight,
            thickness = 4.dp,
        )

        JourneyFilterTab(
            modifier = Modifier.padding(top = 16.dp),
            filters = filters,
            onReferenceChanged = onReferenceChanged,
        )
        JourneyFilterDateTime(
            modifier = Modifier.padding(top = 40.dp),
            filters = filters,
            onDateChanged = onDateChanged,
            onTimeChanged = onTimeChanged,
        )
        JourneyFilterQuickSets(
            modifier = Modifier.padding(top = 24.dp),
            onDateChanged = onDateChanged,
        )

        Box(
            modifier = Modifier
                .padding(top = 48.dp, bottom = 12.dp)
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MTheme.colors.primary)
                .clickable { dismissFilters() },
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                text = R.string.done.stringRes().uppercase(),
                textAlign = TextAlign.Center,
                style = MTheme.type.highlightTitleS.copy(color = Color.White)
            )
        }
    }
}

@Composable
@Preview
fun SearchTripFilterPreview() {
    ResaTheme {
        Column(modifier = Modifier.background(Color.White)) {
            JourneyFilter(
                filters = JourneyFilters(),
                onReferenceChanged = {},
                onDateChanged = {},
                onTimeChanged = {},
                dismissFilters = {},
            )
        }
    }
}
