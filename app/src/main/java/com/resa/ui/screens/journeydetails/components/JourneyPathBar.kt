package com.resa.ui.screens.journeydetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resa.R
import com.resa.global.fake.FakeFactory
import com.resa.ui.screens.journeydetails.state.JourneyDetailsUiEvent
import com.resa.ui.screens.journeydetails.state.JourneyDetailsUiState
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.color
import com.resa.ui.util.fontSize
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun JourneyPathBar(
    modifier: Modifier = Modifier,
    uiState: JourneyDetailsUiState,
    onEvent: (JourneyDetailsUiEvent) -> Unit = {},
) {
    val journey by uiState.selectedJourney.collectAsState()

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .align(Alignment.End)
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = { onEvent(JourneyDetailsUiEvent.OnBackPressed) },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MTheme.colors.btnBackground)
                    .size(32.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_close),
                    contentDescription = null,
                    tint = MTheme.colors.textPrimary,
                )
            }
        }
        Row(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.from),
                style = MTheme.type.textFieldPlaceHolder.copy(
                    color = MTheme.colors.lightText,
                    fontSize = 20.sp,
                ),
            )
            Text(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                text = journey?.originName.orEmpty(),
                maxLines = 2,
                style = MTheme.type.highlightTitleS.fontSize(20.sp),
                overflow = TextOverflow.Visible,
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.to),
                style = MTheme.type.textFieldPlaceHolder
                    .color(MTheme.colors.lightText)
                    .fontSize(20.sp),
            )
            Text(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp)
                    .weight(1f),
                text = journey?.destName.orEmpty(),
                style = MTheme.type.highlightTitleS.copy(fontSize = 20.sp),
                maxLines = 2,
                overflow = TextOverflow.Visible,
            )
        }
    }
}

@Composable
@Preview
fun JourneyPathBarPreview() {
    ResaTheme(darkTheme = false) {
        JourneyPathBar(
            modifier = Modifier.background(color = Color.White),
            uiState = JourneyDetailsUiState(
                selectedJourney = MutableStateFlow(FakeFactory.journey())
            )
        )
    }
}