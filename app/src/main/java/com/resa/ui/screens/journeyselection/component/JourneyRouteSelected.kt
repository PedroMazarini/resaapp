package com.resa.ui.screens.journeyselection.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resa.R
import com.resa.domain.model.LocationType
import com.resa.domain.model.queryjourneys.QueryJourneysParams
import com.resa.ui.application.PayloadType
import com.resa.ui.application.PayloadType.REQUEST_DEST_FOCUS
import com.resa.ui.application.PayloadType.REQUEST_ORIGIN_FOCUS
import com.resa.ui.screens.journeyselection.state.JourneySelectionUiState
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.color
import com.resa.ui.util.fontSize

@Composable
fun JourneyRouteSelected(
    modifier: Modifier = Modifier,
    uiState: JourneySelectionUiState,
    navigateToLocationSearch: (PayloadType) -> Unit,
) {

    val queryParams by uiState.queryParams

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .clickable {
                    navigateToLocationSearch(REQUEST_ORIGIN_FOCUS)
                }
        ) {
            Text(
                text = stringResource(id = R.string.from),
                style = MTheme.type.textFieldPlaceHolder.copy(
                    color = MTheme.colors.lightText,
                    fontSize = 20.sp,
                ),
            )
            Text(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                text = getSelectedName(queryParams, isOrigin = true),
                maxLines = 1,
                style = MTheme.type.highlightTitleS.fontSize(20.sp),
                overflow = TextOverflow.Visible,
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable {
                    navigateToLocationSearch(REQUEST_DEST_FOCUS)
                }
        ) {
            Text(
                text = stringResource(id = R.string.to),
                style = MTheme.type.textFieldPlaceHolder
                    .color(MTheme.colors.lightText)
                    .fontSize(20.sp),
            )
            Text(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp)
                    .weight(1f),
                text = getSelectedName(queryParams, isOrigin = false),
                style = MTheme.type.highlightTitleS.copy(fontSize = 20.sp),
                maxLines = 1,
                overflow = TextOverflow.Visible,
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
            color = MTheme.colors.separatorLight,
            thickness = 1.dp,
        )
    }
}

@Composable
fun getSelectedName(queryParams: QueryJourneysParams, isOrigin: Boolean): String {
    return if (isOrigin) {
        if (queryParams.originType == LocationType.gps) {
            stringResource(id = R.string.current_location)
        } else {
            queryParams.originName.orEmpty()
        }
    } else {
        if (queryParams.destinationType == LocationType.gps) {
            stringResource(id = R.string.current_location)
        } else {
            queryParams.destinationName.orEmpty()
        }
    }
}

@Composable
@Preview
fun TripSearchLocationSelectedPreview() {
    ResaTheme {
        JourneyRouteSelected(
            modifier = Modifier.background(color = Color.White),
            uiState = JourneySelectionUiState(),
            navigateToLocationSearch = {},
        )
    }
}
