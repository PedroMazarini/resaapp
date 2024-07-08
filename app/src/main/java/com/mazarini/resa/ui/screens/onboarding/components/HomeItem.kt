package com.mazarini.resa.ui.screens.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.ui.screens.onboarding.model.DeviceShapeType
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.color
import com.mazarini.resa.ui.util.fontSize
import com.mazarini.resa.ui.util.getFormattedString

@Composable
fun HomeItem(
    modifier: Modifier = Modifier,
) {
    val titleBaseStyle = MTheme.type.highlightTitle.fontSize(34.sp).toSpanStyle()
    val titlePatternStyle =
        MTheme.type.highlightTitle.fontSize(34.sp).color(MTheme.colors.primary).toSpanStyle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier,
            text = getFormattedString(
                resId = R.string.home_onboarding_title,
                baseStyle = titleBaseStyle,
                patternStyle = titlePatternStyle,
            ),
        )

        CheckItem(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 16.dp),
            res = R.string.departures_table,
        )

        CheckItem(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 6.dp),
            res = R.string.saved_journeys,
        )

        CheckItem(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 6.dp),
            res = R.string.recent_journeys,
        )

        DeviceShape(
            modifier = Modifier,
            deviceShapeType = DeviceShapeType.TOP,
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 10.dp, bottom = 20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.onboarding_home),
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
@Preview
fun SearchItemPreview() {
    ResaTheme {
        HomeItem()
    }
}
