package com.mazarini.resa.ui.screens.journeyselection.component

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.LocationType
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysParams
import com.mazarini.resa.ui.screens.journeyselection.state.JourneySelectionUiEvent
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.color
import com.mazarini.resa.ui.util.fontSize
import kotlinx.coroutines.delay

@Composable
fun JourneyRouteSelected(
    modifier: Modifier = Modifier,
    queryParams: QueryJourneysParams,
    showFeatureHighlight: Boolean,
    onEvent: (JourneySelectionUiEvent) -> Unit,
) {

    var showFeatureHighlightAnim = showFeatureHighlight
    var isSaved by remember { mutableStateOf(false) }

    val savedToast = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.journey_saved), Toast.LENGTH_LONG
    )

    Column(modifier = modifier.height(IntrinsicSize.Min)) {
        Row(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Min),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.from),
                    style = MTheme.type.textFieldPlaceHolder.copy(
                        color = MTheme.colors.graph.disabled,
                        fontSize = 14.sp,
                    ),
                )
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = stringResource(id = R.string.to),
                    style = MTheme.type.textFieldPlaceHolder
                        .color(MTheme.colors.graph.disabled)
                        .fontSize(14.sp),
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    text = getSelectedName(queryParams, isOrigin = true),
                    maxLines = 2,
                    style = MTheme.type.highlightTitleS.fontSize(18.sp),
                    overflow = TextOverflow.Visible,
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp),
                    text = getSelectedName(queryParams, isOrigin = false),
                    style = MTheme.type.highlightTitleS.copy(fontSize = 18.sp),
                    maxLines = 2,
                    overflow = TextOverflow.Visible,
                )
            }
            IconButton(
                onClick = {
                    if (isSaved.not())
                        onEvent(JourneySelectionUiEvent.SaveCurrentJourneySearch)
                    savedToast.show()
                    isSaved = true
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 24.dp)
                    .clip(CircleShape)
                    .background(MTheme.colors.btnBackground)
                    .size(38.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(26.dp),
                    painter = getSaveIcon(isSaved),
                    contentDescription = null,
                    tint = MTheme.colors.textPrimary,
                )
            }
        }
        if (showFeatureHighlight) {
            FeatureHighlight(
                modifier = Modifier
                    .align(Alignment.End),
                bubbleModifier = Modifier.padding(end = 24.dp, top = 14.dp),
                pointerModifier = {
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 24.dp)
                },
                visible = showFeatureHighlightAnim,
                message = stringResource(id = R.string.bookmark_search),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            HorizontalDivider(
                color = MTheme.colors.graph.minimal,
                thickness = 1.dp,
            )
        }
    }

    LaunchedEffect(Unit) {
        if (showFeatureHighlightAnim) {
            delay(5000)
            showFeatureHighlightAnim = false
        }
    }
}

@Composable
fun getSaveIcon(isSaved: Boolean): Painter =
    if (isSaved) {
        painterResource(id = R.drawable.ic_check)
    } else {
        painterResource(id = R.drawable.bookmark_outline)
    }

@Composable
fun getSelectedName(queryParams: QueryJourneysParams, isOrigin: Boolean): String {
    return if (isOrigin) {
        if (queryParams.originType == LocationType.gps) {
            stringResource(id = R.string.my_location)
        } else {
            queryParams.originName.orEmpty()
        }
    } else {
        if (queryParams.destinationType == LocationType.gps) {
            stringResource(id = R.string.my_location)
        } else {
            queryParams.destinationName.orEmpty()
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun TripSearchLocationSelectedDarkPreview() {
    ResaTheme(darkTheme = true) {
        JourneyRouteSelected(
            modifier = Modifier,
            queryParams = QueryJourneysParams(
                originName = "Origin",
                destinationName = "Destination",
            ),
            showFeatureHighlight = true,
            onEvent = {},
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun TripSearchLocationSelectedPreview() {
    ResaTheme {
        JourneyRouteSelected(
            modifier = Modifier.background(color = Color.White),
            queryParams = QueryJourneysParams(
                originName = "Origin",
                destinationName = "Destination",
            ),
            showFeatureHighlight = true,
            onEvent = {},
        )
    }
}
