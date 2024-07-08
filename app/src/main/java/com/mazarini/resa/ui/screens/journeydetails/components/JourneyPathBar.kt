package com.mazarini.resa.ui.screens.journeydetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiEvent
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiState
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.color
import com.mazarini.resa.ui.util.fontSize
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun JourneyPathBar(
    modifier: Modifier = Modifier,
    uiState: JourneyDetailsUiState,
    onEvent: (JourneyDetailsUiEvent) -> Unit = {},
) {
    val journey by uiState.selectedJourney.collectAsState()

    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.from),
                style = MTheme.type.textFieldPlaceHolder.copy(
                    color = MTheme.colors.graph.disabled,
                    fontSize = 14.sp,
                ),
            )
            Text(
                modifier = Modifier
                    .weight(1f),
                text = stringResource(id = R.string.to),
                style = MTheme.type.textFieldPlaceHolder
                    .color(MTheme.colors.graph.disabled)
                    .fontSize(14.sp),
            )
        }
        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .weight(1f)
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
                text = journey?.originName.orEmpty(),
                maxLines = 2,
                style = MTheme.type.highlightTitleS.fontSize(18.sp),
                overflow = TextOverflow.Visible,
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp, end = 12.dp)
                    .weight(1f),
                text = journey?.destName.orEmpty(),
                style = MTheme.type.highlightTitleS.copy(fontSize = 18.sp),
                maxLines = 2,
                overflow = TextOverflow.Visible,
            )
        }
    }
}

@Composable
@Preview
fun JourneyPathBarDarkPreview() {
    ResaTheme(darkTheme = true) {
        JourneyPathBar(
            modifier = Modifier,
            uiState = JourneyDetailsUiState(
                selectedJourney = MutableStateFlow(FakeFactory.journey())
            )
        )
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