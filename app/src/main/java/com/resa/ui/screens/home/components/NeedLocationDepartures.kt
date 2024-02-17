package com.resa.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resa.R
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.Previews
import com.resa.ui.util.color
import com.resa.ui.util.fontSize

@Composable
fun NeedLocationDepartures(
    modifier: Modifier = Modifier,
    onLocationRequest: () -> Unit = {},
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.ic_pin),
            contentDescription = null,
            tint = MTheme.colors.primary,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = stringResource(id = R.string.allow_location_departures),
            style = MTheme.type.secondaryText.fontSize(16.sp),
            textAlign = TextAlign.Center,
        )
        Card(
            shape = RoundedCornerShape(4.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(4.dp)
                .clickable { onLocationRequest() },
            colors = CardDefaults.cardColors(
                containerColor = MTheme.colors.primary,
            ),
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = stringResource(id = R.string.allow_location_btn),
                style = MTheme.type.secondaryText
                    .fontSize(16.sp)
                    .color(MTheme.colors.background),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
@Previews
fun NeedLocationDeparturesPreview() {
    ResaTheme {
        NeedLocationDepartures(
            modifier = Modifier.background(Color.White),
        )
    }
}
