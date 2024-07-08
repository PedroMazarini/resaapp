package com.mazarini.resa.ui.commoncomponents

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mazarini.resa.ui.theme.MTheme
import kotlin.math.roundToInt

@Composable
fun DashedLine(
    modifier: Modifier = Modifier,
    step: Dp = 4.dp,
    color: Color = MTheme.colors.lightDetail,
){
    Canvas(
        modifier = modifier.clipToBounds()
    ) {
        // Creating various parameters like step distance,
        // tilt angle, count, size, etc and drawing a straight line
//        val step = step
        val stepPx = step.toPx()
        val stepsCount = (size.height / stepPx).roundToInt()
        val actualStep = size.height / stepsCount
        val dotSize = Size(width = 7f, height = 2f)
        for (i in -4..stepsCount) {
            val rect = Rect(
                offset = Offset(x = i * actualStep, y = (size.height - dotSize.height) / 2),
                size = dotSize,
            )
            rotate(90f) {
                drawRoundRect(
                    color = color,
                    topLeft = rect.topLeft,
                    size = rect.size,
                    cornerRadius = CornerRadius(48f, 48f)
                )
            }
        }
    }
}
