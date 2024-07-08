package com.mazarini.resa.ui.screens.journeydetails.components

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.global.preferences.PrefsProvider
import com.mazarini.resa.ui.commoncomponents.FadeInAnimation
import com.mazarini.resa.ui.commoncomponents.FadeInType
import com.mazarini.resa.ui.commoncomponents.FeatureHighlight
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiEvent
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews
import com.mazarini.resa.ui.util.color
import com.mazarini.resa.ui.util.fontSize
import com.mazarini.resa.ui.util.showMessage
import com.mazarini.resa.ui.util.textAlign
import kotlinx.coroutines.delay

@Composable
fun AddToHomeButton(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onEvent: (JourneyDetailsUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val visibleState = remember { MutableTransitionState(true) }
    val prefsProvider = PrefsProvider(LocalContext.current)
    val showFeatureHighlight = remember { mutableStateOf(false) }
    val showFeatureHighlightAnim = remember { mutableStateOf(false) }
    visibleState.targetState = visible

    FadeInAnimation(
        modifier = modifier,
        fadeInType = FadeInType.BOTTOM,
        visibleState = visibleState,
        exit = { density, offset ->
            slideOutVertically(animationSpec = tween(300)) {
                with(density) { offset.roundToPx() }
            } + fadeOut(
                targetAlpha = 0f,
                animationSpec = tween(300),
            )
        }
    ) {
        Column {
            if (showFeatureHighlight.value && visible) {
                FeatureHighlight(
                    modifier = Modifier
                        .align(Alignment.End),
                    bubbleModifier = Modifier.padding(start = 24.dp, bottom = 14.dp),
                    pointerModifier = {
                        Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 24.dp)
                            .rotate(160f)
                    },
                    visible = showFeatureHighlightAnim.value,
                    message = stringResource(id = R.string.add_to_home_feat),
                )
            }
            FloatingActionButton(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    onEvent(JourneyDetailsUiEvent.SaveJourneyToHome)
                    context.showMessage(context.getString(R.string.added_to_home))
                },
                elevation = FloatingActionButtonDefaults.elevation(12.dp),
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        modifier = Modifier.size(26.dp),
                        painter = painterResource(id = R.drawable.ic_home_add),
                        contentDescription = null,
                        tint = Color.White,
                    )
                    Text(
                        modifier = Modifier,
                        text = stringResource(id = R.string.add_home),
                        style = MTheme.type.textField
                            .fontSize(10.sp)
                            .color(Color.White)
                            .textAlign(TextAlign.Center),
                    )
                }

            }
        }
    }
    LaunchedEffect(Unit) {
        val hasSeen = prefsProvider.getUserPrefs().hasSeenAddHomeFeat
        if (hasSeen.not()) {
            prefsProvider.setSeenAddHomeFeat()
            showFeatureHighlight.value = true
            showFeatureHighlightAnim.value = true
            delay(5000)
            showFeatureHighlightAnim.value = false
        }
    }
}

@Composable
@Previews
fun AddToHomePreview() {
    ResaTheme {
        AddToHomeButton(
            onEvent = { },
            visible = true,
        )
    }
}
