package com.resa.ui.commoncomponents.journeySearchFilters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.resa.R
import com.resa.global.extensions.addDays
import com.resa.global.extensions.stringRes
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.Previews
import java.util.Date

@Composable
fun JourneyFilterQuickSets(
    modifier: Modifier,
    onDateChanged: (Date) -> Unit,
) {

    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Mdivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .clickable { onDateChanged(Date()) },
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 24.dp)
                    .padding(vertical = 16.dp)
                    .size(20.dp),
                painter = painterResource(id = R.drawable.ic_calendar_check),
                contentDescription = null,
                tint = MTheme.colors.lightText,
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 24.dp)
                    .align(Alignment.CenterVertically),
                text = R.string.now.stringRes(),
                style = MTheme.type.textField,
            )
        }
        Mdivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .clickable { onDateChanged(Date().addDays(1)) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 24.dp)
                    .padding(vertical = 16.dp)
                    .size(20.dp),
                painter = painterResource(id = R.drawable.ic_calendar_next),
                contentDescription = null,
                tint = MTheme.colors.lightText,
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 24.dp)
                    .align(Alignment.CenterVertically),
                text = R.string.tomorrow.stringRes(),
                style = MTheme.type.textField,
            )
        }
        Mdivider()
    }
}

@Composable
fun Mdivider() =
    Divider(
        modifier = Modifier.fillMaxWidth(),
        color = MTheme.colors.separatorLight,
        thickness = 1.dp,
    )

@Composable
@Previews
fun JourneyFilterQuickSetsPreview() {
    ResaTheme {
        Column(modifier = Modifier.background(Color.White)) {
            JourneyFilterQuickSets(
                modifier = Modifier,
                onDateChanged = {},
            )
        }
    }
}