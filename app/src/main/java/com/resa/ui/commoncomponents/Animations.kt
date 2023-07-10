package com.resa.ui.commoncomponents

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.resa.R
import com.resa.ui.commoncomponents.FadeInType.*

enum class FadeInType {
    TOP,
    BOTTOM,
    LEFT,
}

@Composable
fun FadeInAnimation(
    fadeInType: FadeInType = TOP,
    animationOffset: Dp = 40.dp,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {
    val visibleState = remember { MutableTransitionState(false) }
    visibleState.targetState = true
    val density = LocalDensity.current
    AnimatedVisibility(
        visibleState,
        enter =
        when (fadeInType) {
            TOP -> slideInVertically(animationSpec = tween(300)) {
                // Slide in from 40 dp from the top.
                with(density) { -animationOffset.roundToPx() }
            }
            BOTTOM -> slideInVertically(animationSpec = tween(300)) {
                // Slide in from 40 dp from the top.
                with(density) { animationOffset.roundToPx() }
            }
            LEFT -> slideInHorizontally(animationSpec = tween(300)) {
                // Slide in from 40 dp from the top.
                with(density) { animationOffset.roundToPx() }
            }

        } + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.1f,
            animationSpec = tween(300)
        ),
        exit = slideOutVertically() + shrinkVertically() + fadeOut(),
        content = content
    )
}

@Composable
fun LottieAnim(
    modifier: Modifier = Modifier,
    animation: Animation,
    contentScale: ContentScale = ContentScale.Fit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animation.resId))
    val animationState by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = { animationState },
        contentScale = contentScale,
    )
}

enum class Animation(val resId: Int) {
    BUS_LOADING(R.raw.bus_loading),
    LIVE(R.raw.live),
    LOADING_LINE(R.raw.loading_line),
    LOADING_SPIN(R.raw.loading_spin),
}

