package com.mazarini.resa.ui.screens.departures.components

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.isNotNull
import com.mazarini.resa.global.extensions.stringRes
import com.mazarini.resa.global.preferences.model.PreferredStop
import com.mazarini.resa.ui.model.Location
import com.mazarini.resa.ui.model.LocationType
import com.mazarini.resa.ui.screens.departures.state.DeparturesUiEvent
import com.mazarini.resa.ui.screens.departures.state.DeparturesUiState
import com.mazarini.resa.ui.screens.locationsearch.components.searchFields.SearchFieldRow
import com.mazarini.resa.ui.screens.locationsearch.components.searchFields.getTextFieldStyle
import com.mazarini.resa.ui.screens.locationsearch.state.CurrentLocation
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews
import com.mazarini.resa.ui.util.showMessage
import java.util.UUID

@Composable
fun DepartureSearchField(
    modifier: Modifier,
    uiState: DeparturesUiState,
    onEvent: (DeparturesUiEvent) -> Unit,
) {
    val selectedStop = uiState.selectedStop
    val isPreferredSelected = uiState.isPreferredSelected
    val searchFocusRequester = remember { FocusRequester() }
    val pinIconRes =
        if (isPreferredSelected) R.drawable.ic_push_pin_filled else R.drawable.ic_push_pin_outline
    val context = LocalContext.current

    Column(modifier = modifier) {
        Row {
            SearchFieldRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 24.dp)
                    .height(24.dp),
                icon = painterResource(id = R.drawable.ic_bus_stop),
                iconModifier = Modifier.height(22.dp),
                textContent = selectedStop?.name ?: uiState.stopQuery,
                placeHolder = R.string.search_stop.stringRes(),
                textStyle = getTextFieldStyle(uiState.selectedStop.isNotNull),
                onCloseClicked = {
                    onEvent(DeparturesUiEvent.OnQueryChanged(""))
                    searchFocusRequester.requestFocus()
                },
                onTextChanged = { onEvent(DeparturesUiEvent.OnQueryChanged(it)) },
                focusRequester = searchFocusRequester,
            )

            selectedStop?.let {
                IconButton(
                    onClick = { sendPreferredAction(isPreferredSelected, context, it, onEvent) },
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .padding(end = 24.dp)
                        .align(Alignment.CenterVertically)
                        .clip(CircleShape)
                        .background(MTheme.colors.btnBackground)
                        .size(32.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxSize(),
                        painter = painterResource(id = pinIconRes),
                        contentDescription = null,
                        tint = MTheme.colors.textPrimary,
                    )
                }
            } ?: run {
                LocationButton(uiState.userLocationRequest, onEvent)
            }
        }
    }
    LaunchedEffect(uiState.requestFocus) {
        if (uiState.requestFocus) searchFocusRequester.requestFocus()
    }
}

@Composable
fun RowScope.LocationButton(
    locationRequest: CurrentLocation?,
    onEvent: (DeparturesUiEvent) -> Unit
) {
    if (locationRequest !is CurrentLocation.Loading) {
        IconButton(
            onClick = { onEvent(DeparturesUiEvent.StartLocationRequest) },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .padding(end = 24.dp)
                .align(Alignment.CenterVertically)
                .clip(CircleShape)
                .background(MTheme.colors.btnBackground)
                .size(32.dp)
        ) {
            Icon(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxSize(),
                painter = painterResource(id = R.drawable.ic_crosshair),
                contentDescription = null,
                tint = MTheme.colors.textPrimary,
            )
        }
    } else {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .padding(end = 24.dp)
                .align(Alignment.CenterVertically)
                .size(24.dp),
            strokeWidth = 2.dp,
            color = MTheme.colors.primary,
            trackColor = MTheme.colors.background,
        )
    }
}

fun sendPreferredAction(
    isPreferredSelected: Boolean,
    context: Context,
    location: Location,
    onEvent: (DeparturesUiEvent) -> Unit
) {
    if (isPreferredSelected) {
        onEvent(DeparturesUiEvent.DeleteCurrentPreferred)
        context.showMessage(context.getString(R.string.preferred_deleted, location.name))
    } else {
        onEvent(DeparturesUiEvent.PinLocation(location))
        context.showMessage(context.getString(R.string.preferred_set, location.name))
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Previews
fun DepartureSearchFieldPreview() {
    ResaTheme {
        DepartureSearchField(
            modifier = Modifier.fillMaxWidth(),
            uiState = DeparturesUiState(
                selectedStop = Location(
                    id = UUID.randomUUID().toString(),
                    name = "Liseberg",
                    type = LocationType.values().random()
                ),
                isPreferredSelected = false,
                preferredStop = PreferredStop(
                    gid = UUID.randomUUID().toString(),
                    name = "Liseberg",
                ),
            ),
            onEvent = {},
        )
    }
}