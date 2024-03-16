package com.resa.ui.screens.journeydetails.components.journeydetailedlegs

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.resa.domain.model.LocationType
import com.resa.domain.model.journey.Leg
import com.resa.global.fake.FakeFactory
import com.resa.ui.screens.journeydetails.components.journeydetailedlegs.legdetails.LegItem
import com.resa.ui.screens.journeydetails.components.journeydetailedlegs.sidebar.LegsSideBar
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.Previews
import com.resa.ui.util.dpToPx
import kotlin.math.roundToInt

@Composable
fun LegsLayout(
    modifier: Modifier = Modifier,
    legs: List<Leg>,
    sideBar: @Composable (leg: Leg) -> Unit,
    legItem: @Composable (leg: Leg) -> Unit,
    isScrolling: (isScrolling: Boolean) -> Unit,
) {
    val transportBar = @Composable { repeat(legs.size) { sideBar(legs[it]) } }
    val legItem = @Composable { repeat(legs.size) { legItem(legs[it]) } }

    val transportPadding = 14.dpToPx()
    val walkPadding = 18.dpToPx()
    val transportExtraHeight = 14.dpToPx()
    val walkextraHeight = 5.dpToPx()
    val sideBarWidth = with(LocalDensity.current) { 56.dp.toPx() }.roundToInt()

    var scrollState by remember { mutableStateOf(0f) }
    val hasScrolled by remember { derivedStateOf { scrollState > 50 } }
    // Calculate the size of the content and the container to determine the maximum scroll distance
    var maxScroll by remember { mutableStateOf(0) }
    // Remember a ScrollableState that handles scroll logic
    val scrollableState = rememberScrollableState { delta ->
        val newValue = scrollState - delta
        scrollState = newValue.coerceIn(0f, maxScroll.toFloat())
        delta
    }

    Layout(
        modifier = modifier.scrollable(scrollableState, orientation = Orientation.Vertical),
        contents = listOf(transportBar, legItem),
    ) {
        (sideBarMeasurables, legItemMeasurables), constraints ->

        val legItemPlaceables = legItemMeasurables.measureAll(constraints, sideBarWidth)
        val sideBarPlaceables = sideBarMeasurables.mapIndexed { index, measurable ->
            val height = getSideBarHeight(
                isLast = index == legs.lastIndex,
                legItemPlaceables[index],
                legs[index],
                transportHeight = transportExtraHeight,
                walkingHeight = walkextraHeight,
            )
            measurable.measure(
                constraints.copy(
                    minHeight = height,
                    maxHeight = height,
                )
            )
        }

        maxScroll = (legItemPlaceables.sumOf { it.height } - constraints.maxHeight).coerceAtLeast(0)

        layout(constraints.maxWidth, constraints.maxHeight) {
            var barYPosition = -scrollState.roundToInt()
            var legYPosition = -scrollState.roundToInt()

            for (i in legItemPlaceables.indices) {
                val transportBarPlaceable = sideBarPlaceables[i]
                val journeyDetailedLegItemPlaceable = legItemPlaceables[i]

                val legY = getSideBarYPosition(
                    legs[i],
                    currentY = barYPosition,
                    transportPadding = transportPadding,
                    walkPadding = walkPadding,
                )

                transportBarPlaceable.placeRelative(0, legY)

                journeyDetailedLegItemPlaceable.placeRelative(
                    transportBarPlaceable.width,
                    legYPosition
                )


                legYPosition += journeyDetailedLegItemPlaceable.height
                barYPosition += journeyDetailedLegItemPlaceable.height
            }
        }
    }
    LaunchedEffect(hasScrolled) {
        isScrolling(hasScrolled)
    }
}

private fun getSideBarYPosition(
    leg: Leg,
    currentY: Int,
    transportPadding: Int = 0,
    walkPadding: Int = 0,
): Int {
    return when (leg) {
        is Leg.Transport -> currentY + transportPadding
        else -> currentY + walkPadding
    }
}

private fun getSideBarHeight(
    isLast: Boolean,
    placeable: Placeable,
    leg: Leg,
    transportHeight: Int,
    walkingHeight: Int,
): Int {
    val height = when (leg) {
        is Leg.Transport -> {
            if (isLast) {
                placeable.height - (2 * transportHeight)
            } else {
                placeable.height + transportHeight
            }
        }
        else -> {
            if (isLast) {
                placeable.height - (7 * walkingHeight)
            } else {
                placeable.height + walkingHeight
            }
        }
    }
    return height.coerceAtLeast(0)
}

private fun List<Measurable>.measureAll(
    constraints: Constraints,
    sideBarWidth: Int
): List<Placeable> {
    return map { measurable ->
        measurable.measure(
            constraints.copy(
                maxWidth = constraints.maxWidth - sideBarWidth,
                maxHeight = Constraints.Infinity,
            )
        )
    }
}

@Composable
@Preview
fun LegsLayoutPreview() {
    ResaTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            LegsLayout(
                legs = FakeFactory.legList(),
                sideBar = { leg ->
                    LegsSideBar(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        leg = leg,
                    )
                },
                legItem = { leg ->
                    LegItem(
                        leg = leg,
                    )
                },
                isScrolling = {},
            )
        }
    }
}
