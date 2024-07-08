package com.mazarini.resa.ui.commoncomponents.animations

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mazarini.resa.ui.theme.colors.YellowGreen

@Composable
fun PulsatingCircle(
    modifier: Modifier,
) {
    Pulsating(pulseFraction = 3f) {
        Surface(
            color = YellowGreen,
            shape = CircleShape,
            modifier = modifier,
            content = {}
        )
    }
}