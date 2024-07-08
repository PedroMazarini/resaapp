package com.mazarini.resa.ui.screens.onboarding.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
fun SearchItem(
    modifier: Modifier = Modifier,
) {
    val titleBaseStyle = MTheme.type.highlightTitle.fontSize(34.sp).toSpanStyle()
    val titlePatternStyle = MTheme.type.highlightTitle.fontSize(34.sp).color(MTheme.colors.primary).toSpanStyle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            modifier = Modifier,
                text = getFormattedString(
                resId = R.string.search_title,
                baseStyle = titleBaseStyle,
                patternStyle = titlePatternStyle,
            )
        )

        CheckItem(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 16.dp),
            res = R.string.search_where_togo,
        )

        CheckItem(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 6.dp),
            res = R.string.search_gps,
        )

        CheckItem(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 6.dp),
            res = R.string.search_pin,
        )

        DeviceShape(
            modifier = Modifier,
            deviceShapeType = DeviceShapeType.TOP,
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 12.dp, bottom = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.onboarding_search),
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun CheckItem(
    modifier: Modifier,
    @StringRes res: Int,
    style: TextStyle = MTheme.type.highlightTitle.fontSize(24.sp),
) {
    val text = getFormattedString(
        resId = res,
        baseStyle = style.toSpanStyle(),
        patternStyle = style.color(MTheme.colors.primary).toSpanStyle(),
    )
    Row(
        modifier = modifier,
    ) {
        Icon(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(24.dp),
            painter = painterResource(id = R.drawable.ic_check),
            contentDescription = null,
            tint = MTheme.colors.primary,
        )
        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = text,
        )
    }
}

@Composable
@Preview
fun HomeItemPreview() {
    ResaTheme {
        SearchItem()
    }
}
