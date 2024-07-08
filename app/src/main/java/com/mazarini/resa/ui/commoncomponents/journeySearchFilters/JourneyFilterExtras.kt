package com.mazarini.resa.ui.commoncomponents.journeySearchFilters

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.TransportMode
import com.mazarini.resa.domain.model.TransportMode.Companion.stringRes
import com.mazarini.resa.global.extensions.stringRes
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state.JourneyFilterUiEvent
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state.JourneyFilterUiEvent.OnModesClicked
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state.JourneyFilterUiState
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme

@Composable
fun JourneyFilterExtras(
    modifier: Modifier = Modifier,
    uiState: JourneyFilterUiState,
    onEvent: (JourneyFilterUiEvent) -> Unit,
) {
    val preferredModes by remember { uiState.preferredModes }
    val preferredModesText = getSelectedModesText(preferredModes = preferredModes)

    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Mdivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .clickable { onEvent(OnModesClicked) },
        ) {
            Column (
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .padding(start = 24.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = R.string.transport_mode.stringRes(),
                    style = MTheme.type.textField,
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = preferredModesText,
                    style = MTheme.type.textField.copy(fontWeight = FontWeight.Normal, fontSize = 12.sp),
                )
            }
        }
    }
}

@Composable
fun getSelectedModesText(preferredModes: List<TransportMode>?) =
    R.string.selected.stringRes().lowercase()+": " +
            preferredModes
                ?.map { it.stringRes().stringRes().lowercase() }
                ?.joinToString { it }


@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun JourneyFilterExtrasDarkPreview() {
    ResaTheme(darkTheme = true) {
        Column {
            JourneyFilterExtras(
                modifier = Modifier,
                uiState = JourneyFilterUiState(),
                onEvent = {},
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun JourneyFilterExtrasPreview() {
    ResaTheme {
        Column(modifier = Modifier.background(Color.White)) {
            JourneyFilterExtras(
                modifier = Modifier,
                uiState = JourneyFilterUiState(),
                onEvent = {},
            )
        }
    }
}