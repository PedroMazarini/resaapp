package com.resa.ui.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.resa.ui.screens.journeyselection.component.shimmerEffect
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.Previews

@Composable
fun LoadingDepartures() {
    Column {
        repeat(3) {
            DepartureShimmer(modifier = Modifier.padding(bottom = 12.dp))
        }
    }
}

@Composable
fun DepartureShimmer(
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            repeat(6) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(15.dp)
                            .shimmerEffect()
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .height(15.dp)
                            .shimmerEffect()
                    )
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(15.dp)
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}

@Composable
@Previews
fun LoadingDeparturesPreview() {
    ResaTheme {
        LoadingDepartures()
    }
}
