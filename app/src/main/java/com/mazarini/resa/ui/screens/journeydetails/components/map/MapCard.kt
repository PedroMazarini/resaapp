package com.mazarini.resa.ui.screens.journeydetails.components.map

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.NTuple4
import com.mazarini.resa.global.extensions.asCardBackground
import com.mazarini.resa.global.extensions.asCardElevation
import com.mazarini.resa.global.extensions.then
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiEvent
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiState
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.color
import com.mazarini.resa.ui.util.fontSize
import com.mazarini.resa.ui.util.textAlign

@Composable
fun MapCard(
    modifier: Modifier = Modifier,
    uiState: JourneyDetailsUiState,
    onEvent: (JourneyDetailsUiEvent) -> Unit,
) {

    val isTrackingAvailable by remember { uiState.isTrackingAvailable }
    val (trackingText, trackingOutline, trackingBg, trackingIcon) = getResourcesId(
        isTrackingAvailable
    )

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp.asCardElevation(),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(4f)
            .clickable {
                onEvent(JourneyDetailsUiEvent.SetShouldShowMap(true))
                onEvent(JourneyDetailsUiEvent.StartTrackingVehicles)
            },
        colors = MTheme.colors.surface.asCardBackground(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp),
                ) {
                    Row(modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterVertically),
                            text = stringResource(id = R.string.see_on_map),
                            style = MTheme.type.textField,
                        )
                        Box(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .padding(horizontal = 16.dp)
                                .clip(CircleShape)
                                .background(MTheme.colors.graph.minimal)
                                .size(32.dp)
                                .align(Alignment.CenterVertically),
                        ) {
                            Icon(
                                modifier = Modifier.align(Alignment.Center),
                                painter = painterResource(id = R.drawable.ic_arrow_right_alt),
                                contentDescription = null,
                                tint = MTheme.colors.textPrimary,
                            )
                        }
                    }
                    Image(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(32.dp)
                            .align(Alignment.CenterVertically),
                        painter = painterResource(id = R.drawable.ic_maps),
                        contentDescription = null,
                    )
                }
                LiveTrackingCard(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .padding(vertical = 8.dp)
                        .align(Alignment.BottomStart),
                    trackingText = trackingText,
                    trackingOutline = trackingOutline,
                    trackingBg = trackingBg,
                    trackingIcon = trackingIcon,
                )
            }
        }
    }
}

@Composable
fun LiveTrackingCard(
    modifier: Modifier,
    trackingText: String,
    trackingOutline: Color,
    trackingBg: Color,
    trackingIcon: Painter
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        elevation = 2.dp.asCardElevation(),
        colors = trackingBg.asCardBackground(),
        border = BorderStroke(1.dp, trackingOutline),
    ) {
        Row(
            modifier = Modifier
                .padding(2.dp),
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 2.dp)
                    .padding(vertical = 1.dp)
                    .size(12.dp)
                    .align(Alignment.CenterVertically),
                painter = trackingIcon,
                contentDescription = null,
                tint = trackingOutline,
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .align(Alignment.CenterVertically),
                text = trackingText,
                style = MTheme.type.fadedTextS.copy(fontSize = 10.sp),
            )
        }
    }
}

@Composable
private fun getResourcesId(isTrackingAvailable: Boolean): NTuple4<String, Color, Color, Painter> =
    if (isTrackingAvailable) {
        stringResource(id = R.string.tracking_available) then
                MTheme.colors.trackingOutline then
                MTheme.colors.trackingBg then
                painterResource(id = R.drawable.ic_pin)
    } else {
        stringResource(R.string.tracking_not_available) then
                MTheme.colors.graph.normal then
                MTheme.colors.surface then
                painterResource(id = R.drawable.ic_link_off)
    }

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun MapCardTrackingDarkPreview() {
    ResaTheme(darkTheme = true) {
        MapCard(
            modifier = Modifier,
            uiState = JourneyDetailsUiState(
                isTrackingAvailable = mutableStateOf(true),
            ),
            onEvent = {},
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun MapCardDarkPreview() {
    ResaTheme(darkTheme = true) {
        MapCard(
            modifier = Modifier,
            uiState = JourneyDetailsUiState(
                isTrackingAvailable = mutableStateOf(false),
            ),
            onEvent = {},
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun MapCardTrackingPreview() {
    ResaTheme {
        MapCard(
            modifier = Modifier.background(Color.White),
            uiState = JourneyDetailsUiState(
                isTrackingAvailable = mutableStateOf(true),
            ),
            onEvent = {},
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun MapCardPreview() {
    ResaTheme {
        MapCard(
            modifier = Modifier.background(Color.White),
            uiState = JourneyDetailsUiState(
                isTrackingAvailable = mutableStateOf(false),
            ),
            onEvent = {},
        )
    }
}