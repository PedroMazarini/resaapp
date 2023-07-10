package com.resa.ui.screens.journeyqueryresult

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.resa.ui.screens.journeyqueryresult.state.JourneyQueryResultUiState
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.Previews

@Composable
fun JourneyQueryResultScreen(
    modifier: Modifier = Modifier,
    uiState: JourneyQueryResultUiState,
) {

    val test by uiState.test.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = test,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
@Previews
fun JourneyQueryResultScreenPreview() {
    ResaTheme {
        JourneyQueryResultScreen(
            uiState = JourneyQueryResultUiState(),
        )
    }
}
