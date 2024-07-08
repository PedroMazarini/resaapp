package com.mazarini.resa.ui.screens.departures.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.asCardElevation
import com.mazarini.resa.global.extensions.isNotNull
import com.mazarini.resa.global.extensions.isNull
import com.mazarini.resa.global.extensions.tryCatch
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.global.preferences.model.PreferredStop
import com.mazarini.resa.ui.model.Location
import com.mazarini.resa.ui.model.LocationType
import com.mazarini.resa.ui.screens.departures.state.DeparturesUiEvent
import com.mazarini.resa.ui.screens.departures.state.DeparturesUiState
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
@OptIn(ExperimentalLayoutApi::class)
fun RecentStopChipList(
    uiState: DeparturesUiState,
    onEvent: (DeparturesUiEvent) -> Unit
) {
    val selectedStop by uiState.selectedStop.collectAsState()
    val showStopQueryResult by remember { uiState.showStopQueryResult }
    val recentLocations by remember { uiState.recentLocations }
    val isPreferredSelected by remember { uiState.isPreferredSelected }
    val preferredStop by uiState.preferredStop.collectAsState()
    val keyboard = LocalSoftwareKeyboardController.current

    if (!showStopQueryResult && selectedStop.isNull || (!isPreferredSelected && preferredStop.isNotNull)) {
        Column {
            preferredStop?.let {
                if (!isPreferredSelected) {
                    RecentStopChipItem(
                        modifier = Modifier.padding(start = 24.dp),
                        text = it.name,
                        showPin = true,
                    ) {
                        keyboard?.hide()
                        onEvent(DeparturesUiEvent.OnStopSelected(
                            Location(id = it.gid, name = it.name, type = LocationType.stoparea)
                        ))
                    }
                }
            }

            if (!showStopQueryResult && selectedStop.isNull) {
                Text(
                    modifier = Modifier.padding(start = 24.dp, top = 16.dp),
                    text = "${stringResource(id = R.string.recent)}:",
                    style = MTheme.type.secondaryText,
                )

                FlowRow(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    recentLocations.forEach { location ->
                        RecentStopChipItem(
                            text = getLocationName(location),
                        ) {
                            keyboard?.hide()
                            onEvent(DeparturesUiEvent.OnStopSelected(location))
                        }
                    }
                }
            }
        }
    }
}

fun getLocationName(location: Location): String =
    tryCatch { location.name.split(",").first() } ?: location.name

@Composable
private fun RecentStopChipItem(
    modifier: Modifier = Modifier,
    text: String,
    showPin: Boolean = false,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp.asCardElevation(),
        colors = CardDefaults.cardColors(
            containerColor = MTheme.colors.surface,
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier,
                text = text,
                style = MTheme.type.secondaryText,
            )
            if (showPin) {
                Icon(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(12.dp),
                    painter = painterResource(id = R.drawable.ic_push_pin_filled),
                    contentDescription = null,
                    tint = MTheme.colors.textPrimary,
                )
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Previews
fun RecentStopChipItemPreview() {
    ResaTheme {
        RecentStopChipList(
            uiState = DeparturesUiState(
                recentLocations = mutableStateOf(FakeFactory.locationList()),
                isPreferredSelected = mutableStateOf(false),
                preferredStop = MutableStateFlow(PreferredStop("1", "Liseberg"))
            ),
            onEvent = { }
        )
    }
}