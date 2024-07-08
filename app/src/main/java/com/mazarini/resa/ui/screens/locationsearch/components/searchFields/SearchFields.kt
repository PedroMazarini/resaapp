package com.mazarini.resa.ui.screens.locationsearch.components.searchFields

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.isNotNull
import com.mazarini.resa.global.extensions.stringRes
import com.mazarini.resa.ui.commoncomponents.permissions.location.LocationAction
import com.mazarini.resa.ui.commoncomponents.permissions.location.RequestLocation
import com.mazarini.resa.ui.model.LocationType
import com.mazarini.resa.ui.screens.locationsearch.state.CurrentLocation
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.ClearDest
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.ClearOrigin
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.DestFocusChanged
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.DestSearchChanged
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.OriginFocusChanged
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.OriginSearchChanged
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.SwapLocations
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiState
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme

@Composable
fun SearchFields(
    modifier: Modifier = Modifier,
    uiState: LocationSearchUiState,
    onEvent: (LocationSearchUiEvent) -> Unit,
) {
    val originSearch by getOriginSearchText(uiState)
    val destSearch by getDestSearchText(uiState)
    val originFocusRequest by uiState.requestOriginFocus
    val destFocusRequest by uiState.requestDestFocus
    val locationRequest by uiState.currentLocationRequest
    val originFocusRequester = remember { FocusRequester() }
    val destFocusRequester = remember { FocusRequester() }
    val customTextSelectionColors = TextSelectionColors(
        handleColor = MTheme.colors.primary,
        backgroundColor = MTheme.colors.surfaceBlur,
    )

    Column(
        modifier = modifier
            .background(MTheme.colors.background)
            .fillMaxWidth(),
    ) {
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            SearchFieldRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 29.dp, bottom = 10.dp)
                    .height(24.dp),
                icon = painterResource(id = R.drawable.ic_trip_origin),
                iconModifier = Modifier.size(12.dp),
                textContent = originSearch,
                placeHolder = R.string.origin.stringRes(),
                focusRequester = originFocusRequester,
                textStyle = getOriginTextStyle(uiState),
                onCloseClicked = { onEvent(ClearOrigin) },
                onTextChanged = { onEvent(OriginSearchChanged(it)) },
                onFocusChanged = { onEvent(OriginFocusChanged(it)) },
            )
        }

        SearchFieldSeparatorRow(onSwitchLocations = { onEvent(SwapLocations) })

        SearchFieldRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 29.dp)
                .height(24.dp),
            icon = painterResource(id = R.drawable.ic_pin),
            iconModifier = Modifier.height(22.dp),
            textContent = destSearch,
            placeHolder = R.string.destination.stringRes(),
            focusRequester = destFocusRequester,
            textStyle = getDestTextStyle(uiState),
            onCloseClicked = { onEvent(ClearDest) },
            onTextChanged = { onEvent(DestSearchChanged(it)) },
            onFocusChanged = { onEvent(DestFocusChanged(it)) },
        )

        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = MTheme.colors.graph.minimal,
            thickness = 1.dp,
        )

        CurrentLocationButton(
            modifier = Modifier
                .clickable {
                    onEvent(LocationSearchUiEvent.RequestLocation(CurrentLocation.Request))
                },
            uiState = uiState,
        )

        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = MTheme.colors.graph.minimal,
            thickness = 1.dp,
        )
    }

    if (locationRequest is CurrentLocation.Request) {
        onEvent(LocationSearchUiEvent.RequestLocation(CurrentLocation.Loading))
        RequestLocationPermission(onEvent)
    }

    SideEffect {
        if (originFocusRequest) {
            destFocusRequester.freeFocus()
            originFocusRequester.requestFocus()
        } else if (destFocusRequest) {
            originFocusRequester.freeFocus()
            destFocusRequester.requestFocus()
        }
    }
    LaunchedEffect(Unit) {
        originFocusRequester.requestFocus()
    }
}

@Composable
fun RequestLocationPermission(onEvent: (LocationSearchUiEvent) -> Unit) {
    val locationFailed = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.location_failed),
        Toast.LENGTH_LONG,
    )
    val myLocationName = stringResource(id = R.string.my_location)
    RequestLocation {
        onEvent(LocationSearchUiEvent.RequestLocation(null))
        when (it) {
            is LocationAction.OnSuccess -> {
                onEvent(
                    LocationSearchUiEvent.LocationResult(
                    name = myLocationName,
                    lat = it.lat,
                    lon = it.lon,
                ))
            }
            else -> {
                locationFailed.show()
            }
        }
    }
}

@Composable
fun getOriginSearchText(uiState: LocationSearchUiState): State<String> {
    val originSearch by uiState.originSearch.collectAsState()
    val origin by uiState.originSelected.collectAsState()
    val currentText = stringResource(id = R.string.my_location)
    return remember {
        derivedStateOf {
            val selected = origin
            when {
                selected?.type == LocationType.gps -> currentText
                selected != null -> selected.name
                else -> originSearch
            }
        }
    }
}

@Composable
fun getDestSearchText(uiState: LocationSearchUiState): State<String> {
    val destSearch by uiState.destSearch.collectAsState()
    val dest by uiState.destSelected.collectAsState()
    val currentText = stringResource(id = R.string.my_location)
    return remember {
        derivedStateOf {
            val selected = dest
            when {
                selected?.type == LocationType.gps -> currentText
                selected != null -> selected.name
                else -> destSearch
            }
        }
    }
}

@Composable
fun getOriginTextStyle(uiState: LocationSearchUiState): TextStyle {
    val selected by uiState.originSelected.collectAsState()
    return getTextFieldStyle(selected.isNotNull)
}

@Composable
fun getDestTextStyle(uiState: LocationSearchUiState): TextStyle {
    val selected by uiState.originSelected.collectAsState()
    return getTextFieldStyle(selected.isNotNull)
}

@Composable
fun getTextFieldStyle(selected: Boolean): TextStyle =
    if (selected.isNotNull) {
        MTheme.type.textField.copy(textDecoration = TextDecoration.Underline)
    } else {
        MTheme.type.textField
    }

@Composable
@Preview
fun SearchFieldsPreview() {
    ResaTheme {
        Column(modifier = Modifier.background(Color.White)) {
            SearchFields(
                uiState = LocationSearchUiState(),
                onEvent = {},
            )
        }
    }
}
