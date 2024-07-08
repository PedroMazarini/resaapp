package com.mazarini.resa.ui.screens.departures.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.commoncomponents.LocationName
import com.mazarini.resa.ui.screens.departures.state.DeparturesUiEvent
import com.mazarini.resa.ui.screens.departures.state.DeparturesUiState
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun StopQueryResultList(
    uiState: DeparturesUiState,
    onEvent: (DeparturesUiEvent) -> Unit,
) {
    val stopQueryResult by remember { uiState.stopQueryResult }
    val stopQuery = uiState.stopQuery.collectAsState().value

    LazyColumn(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        items(stopQueryResult) { location ->
            Column(modifier = Modifier.clickable { onEvent(DeparturesUiEvent.OnStopSelected(location)) }) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MTheme.colors.graph.minimal,
                    thickness = 1.dp,
                )
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LocationName(
                        modifier = Modifier.padding(vertical = 12.dp),
                        location = location,
                        search = stopQuery,
                    )
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Previews
fun StopQueryResultListPreview() {
    ResaTheme {
        StopQueryResultList(
            uiState = DeparturesUiState(
                stopQueryResult = mutableStateOf(FakeFactory.locationList()),
            ),
            onEvent = {},
        )
    }
}