package com.mazarini.resa.ui.commoncomponents.journeySearchFilters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.addDays
import com.mazarini.resa.global.extensions.asCardBackground
import com.mazarini.resa.global.extensions.asCardElevation
import com.mazarini.resa.global.extensions.stringRes
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import java.util.Date

@Composable
fun JourneyFilterQuickSets(
    modifier: Modifier,
    onDateChanged: (Date) -> Unit,
) {

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable { onDateChanged(Date()) },
            shape = RoundedCornerShape(16.dp),
            elevation = 0.dp.asCardElevation(),
            colors = MTheme.colors.btnBackground.asCardBackground(),
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                text = R.string.now.stringRes(),
                style = MTheme.type.textFieldPlaceHolder.copy(fontSize = 12.sp),
            )
        }
        Card(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
                .clickable { onDateChanged(Date().addDays(1)) },
            shape = RoundedCornerShape(16.dp),
            elevation = 0.dp.asCardElevation(),
            colors = MTheme.colors.btnBackground.asCardBackground(),
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                text = R.string.tomorrow.stringRes(),
                style = MTheme.type.textFieldPlaceHolder.copy(fontSize = 12.sp),
            )
        }
    }
}

@Composable
fun Mdivider(modifier: Modifier = Modifier) =
    Divider(
        modifier = modifier.fillMaxWidth(),
        color = MTheme.colors.graph.minimal,
        thickness = 1.dp,
    )

@Composable
@Preview
fun JourneyFilterQuickSetsDarkPreview() {
    ResaTheme(darkTheme = true) {
        Column {
            JourneyFilterQuickSets(
                modifier = Modifier,
                onDateChanged = {},
            )
        }
    }
}

@Composable
@Preview
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