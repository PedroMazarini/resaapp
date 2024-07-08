package com.mazarini.resa.ui.screens.departures.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mazarini.resa.ui.screens.journeyselection.component.shimmerEffect
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun LoadingStops() {
    Column {
        repeat(20) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = MTheme.colors.graph.minimal,
                thickness = 1.dp,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
                    .height(15.dp)
                    .shimmerEffect()
            )
        }
    }
}

@Composable
@Previews
fun LoadingStopsPreview() {
    ResaTheme {
        LoadingStops()
    }
}
