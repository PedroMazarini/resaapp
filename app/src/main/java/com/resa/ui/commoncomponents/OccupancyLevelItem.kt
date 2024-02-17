package com.resa.ui.commoncomponents

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
import com.resa.R
import com.resa.domain.model.journey.OccupancyLevel
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.Previews

@Composable
fun OccupancyLevelItem(
    modifier: Modifier = Modifier,
    occupancyLevel: OccupancyLevel,
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .border(BorderStroke(1.dp, MTheme.colors.lightDetail))
            .background(color = MTheme.colors.ultraLightDetail),
    ) {
        Row {
            Icon(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .padding(start = 4.dp)
                    .height(18.dp),
                painter = painterResource(id = R.drawable.ic_man),
                tint = MTheme.colors.textSecondary,
                contentDescription = null,
            )
            Icon(
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 2.dp)
                    .height(18.dp),
                painter = painterResource(id = R.drawable.ic_man),
                contentDescription = null,
                tint = if (occupancyLevel != OccupancyLevel.LOW)
                    MTheme.colors.textSecondary
                else MTheme.colors.lightDetail
            )
            Icon(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .padding(end = 4.dp)
                    .height(18.dp),
                painter = painterResource(id = R.drawable.ic_man),
                contentDescription = null,
                tint = if (occupancyLevel == OccupancyLevel.HIGH)
                    MTheme.colors.textSecondary
                else MTheme.colors.lightDetail
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
