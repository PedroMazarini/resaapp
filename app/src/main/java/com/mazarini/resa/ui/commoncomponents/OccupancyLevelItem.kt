package com.mazarini.resa.ui.commoncomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.journey.OccupancyLevel
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun OccupancyLevelItem(
    modifier: Modifier = Modifier,
    occupancyLevel: OccupancyLevel,
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .border(BorderStroke(1.dp, MTheme.colors.lightDetail))
            .background(color = MTheme.colors.surface),
    ) {
        Row {
            val icon = when (occupancyLevel) {
                OccupancyLevel.LOW -> R.drawable.occupancy_one
                OccupancyLevel.MEDIUM -> R.drawable.occupancy_two
                OccupancyLevel.HIGH -> R.drawable.occupancy_three
                OccupancyLevel.UNKNOWN -> R.drawable.occupancy_one
            }
            Icon(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .padding(horizontal = 4.dp)
                    .height(18.dp),
                painter = painterResource(id = icon),
                tint = MTheme.colors.textSecondary,
                contentDescription = null,
            )
        }
    }
}

@Composable
@Previews
fun OccupancyLevelPreview() {
    ResaTheme {
        OccupancyLevelItem(
            modifier = Modifier.background(color = MTheme.colors.background),
            occupancyLevel = OccupancyLevel.MEDIUM,
        )
    }
}

@Composable
@Previews
fun OccupancyLevelHighPreview() {
    ResaTheme {
        OccupancyLevelItem(
            modifier = Modifier.background(color = MTheme.colors.background),
            occupancyLevel = OccupancyLevel.HIGH,
        )
    }
}
