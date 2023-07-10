package com.resa.ui.screens.locationsearch.components.searchFields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.resa.R
import com.resa.ui.commoncomponents.DashedLine
import com.resa.ui.theme.MTheme

@Composable
fun SearchFieldSeparatorRow(
    onSwitchLocations: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(41.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                DashedLine(
                    Modifier
                        .height(41.dp)
                        .width(8.dp)
                        .align(Alignment.Center)
                )
            }

            Divider(
                modifier = Modifier.padding(start = 72.dp, end = 24.dp),
                color = MTheme.colors.ultraLightDetail,
                thickness = 1.dp,
            )
        }

        Box(
            modifier = Modifier
                .width(56.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = { onSwitchLocations() },
                modifier = Modifier
                    .padding(end = 24.dp)
                    .size(24.dp)
            ) {
                Icon(
                    modifier = Modifier.size(26.dp),
                    painter = painterResource(id = R.drawable.ic_switch),
                    contentDescription = null,
                    tint = MTheme.colors.textPrimary,
                )
            }
        }
    }
}
