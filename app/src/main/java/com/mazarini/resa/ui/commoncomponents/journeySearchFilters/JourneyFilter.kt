package com.mazarini.resa.ui.commoncomponents.journeySearchFilters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.stringRes
import com.mazarini.resa.ui.commoncomponents.dialogs.TransportModesDialog
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state.JourneyFilterUiEvent
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state.JourneyFilterUiEvent.OnDateChanged
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state.JourneyFilterUiEvent.OnReferenceChanged
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state.JourneyFilterUiEvent.OnTimeChanged
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state.JourneyFilterUiState
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme

@Composable
fun JourneyFilter(
    modifier: Modifier = Modifier,
    uiState: JourneyFilterUiState,
    onEvent: (JourneyFilterUiEvent) -> Unit,
    dismissFilters: () -> Unit,
) {

    val filters = uiState.filters
    val showModesDialog = remember { mutableStateOf(false) }

    Column(
        modifier
            .fillMaxWidth()
            .background(color = MTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalDivider(
            modifier = Modifier
                .padding(top = 4.dp)
                .height(4.dp)
                .width(56.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MTheme.colors.graph.minimal,
            thickness = 4.dp,
        )

        JourneyFilterTab(
            modifier = Modifier.padding(top = 16.dp),
            filters = filters,
            onReferenceChanged = { onEvent(OnReferenceChanged(it)) },
        )
        JourneyFilterDateTime(
            modifier = Modifier.padding(top = 40.dp),
            filters = filters,
            onDateChanged = { onEvent(OnDateChanged(it)) },
            onTimeChanged = { onEvent(OnTimeChanged(it)) },
        )
        JourneyFilterQuickSets(
            modifier = Modifier
                .padding(top = 8.dp, start = 24.dp),
            onDateChanged = { onEvent(OnDateChanged(it)) },
        )

        JourneyFilterExtras(
            modifier = Modifier.padding(top = 16.dp),
            uiState = uiState,
            onEvent = { interceptEvents(it, onEvent, showModesDialog) },
        )

        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(top = 32.dp)
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
    if (showModesDialog.value) {
        TransportModesDialog(
            onResult = { modes ->
                onEvent(JourneyFilterUiEvent.OnModesChanged(modes))
                showModesDialog.value = false
                       },
            preferredModes = uiState.preferredModes,
            onDismiss = { showModesDialog.value = false },
        )
    }
}

fun interceptEvents(
    event: JourneyFilterUiEvent,
    onEvent: (JourneyFilterUiEvent) -> Unit,
    showModesDialog: MutableState<Boolean>,
) {
    when (event) {
        is JourneyFilterUiEvent.OnModesClicked -> showModesDialog.value = true
        else -> onEvent(event)
    }
}

@Composable
@Preview
fun SearchTripFilterDarkPreview() {
    ResaTheme(darkTheme = true) {
        Column {
            JourneyFilter(
                uiState = JourneyFilterUiState(),
                onEvent = {},
                dismissFilters = {},
            )
        }
    }
}

@Composable
@Preview
fun SearchTripFilterPreview() {
    ResaTheme {
        Column {
            JourneyFilter(
                uiState = JourneyFilterUiState(),
                onEvent = {},
                dismissFilters = {},
            )
        }
    }
}
