package com.mazarini.resa.ui.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mazarini.resa.R
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun SavedJourneyAdd(
    modifier: Modifier = Modifier,
    onAddClicked: () -> Unit = {},
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .width(200.dp)
            .padding(horizontal = 4.dp)
            .height(138.dp)
            .testTag("SavedJourneyAdd")
            .clickable { onAddClicked() },
        border = BorderStroke(2.dp, MTheme.colors.primary),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MTheme.colors.background)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MTheme.colors.primary),
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(id = R.string.add_your_trips),
                style = MTheme.type.fadedTextS,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
@Previews
fun SavedJourneyAddPreview() {
    ResaTheme {
        SavedJourneyAdd()
    }
}
