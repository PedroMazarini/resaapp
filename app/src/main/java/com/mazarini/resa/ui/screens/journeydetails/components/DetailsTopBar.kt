package com.mazarini.resa.ui.screens.journeydetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mazarini.resa.R
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiEvent
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiState
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme


@Composable
fun DetailsTopBar(
    modifier: Modifier = Modifier,
    uiState: JourneyDetailsUiState,
    onEvent: (JourneyDetailsUiEvent) -> Unit = {},
) {

    Row(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center,
        ) {
            IconButton(
                onClick = { onEvent(JourneyDetailsUiEvent.OnBackPressed) },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MTheme.colors.btnBackground)
                    .size(32.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = MTheme.colors.textPrimary,
                )
            }
        }
        JourneyPathBar(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f),
            uiState = uiState,
            onEvent = onEvent,
        )
    }
}

@Composable
@Preview
fun DetailsTopBarPreview() {
    ResaTheme(darkTheme = false) {
        DetailsTopBar(
            modifier = Modifier.background(color = Color.White),
            uiState = JourneyDetailsUiState(),
        )
    }
}