package com.mazarini.resa.ui.screens.journeyselection.component

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.mazarini.resa.ui.commoncomponents.FadeInAnimation
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun FeatureHighlight(
    modifier: Modifier = Modifier,
    bubbleModifier: Modifier = Modifier,
    pointerModifier: BoxScope.() -> Modifier = { Modifier },
    visible: Boolean,
    message: String,
) {
    val visibleState = remember { MutableTransitionState(visible) }
    visibleState.targetState = visible
    FadeInAnimation(
        visibleState = visibleState,
        exit = { _, _ -> shrinkOut() + fadeOut() },
    ) {
        Box(modifier = modifier.clickable {
            visibleState.targetState = false
        }) {
            Surface(
                modifier = bubbleModifier
                    .clip(RoundedCornerShape(8.dp)),
                shadowElevation = 8.dp,
                color = MTheme.colors.primary,
            ) {
                Text(
                    modifier = Modifier
                        .padding(12.dp),
                    text = message,
                    style = MTheme.type.highlightTitle.copy(color = Color.White),
                )
            }
            TriangleShape(
                modifier = pointerModifier()
                    .padding(horizontal = 12.dp)
                    .height(20.dp)
                    .width(12.dp)
            )
        }
    }
}

@Composable
fun TriangleShape(modifier: Modifier) {
    val color = MTheme.colors.primary
    Canvas(modifier = modifier) {
        val path = Path().apply {
            // Define the points of the triangle
            moveTo(size.width / 2, 0f) // Top point
            lineTo(size.width, size.height) // Bottom right point
            lineTo(0f, size.height) // Bottom left point
            close() // Close the path to form the triangle
        }
        drawPath(
            path = path,
            color = color,
        )
    }
}

@Composable
@Previews
fun FeatureHighlightPreview() {
    ResaTheme {
        FeatureHighlight(
            message = "This is a feature highlight",
            visible = true,
            pointerModifier = {
                Modifier
                    .align(Alignment.BottomEnd)
                    .rotate(160f)
            }
        )
    }
}
