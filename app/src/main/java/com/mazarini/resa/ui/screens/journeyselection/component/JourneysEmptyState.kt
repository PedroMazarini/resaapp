package com.mazarini.resa.ui.screens.journeyselection.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.mazarini.resa.R
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun JourneysEmptyState(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(top = 16.dp)
            .fillMaxSize()
            .background(color = MTheme.colors.background),
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(horizontal = 24.dp),
            painter = painterResource(id = R.drawable.empty_state),
            contentDescription = null,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp),
            text = stringResource(id = R.string.no_journeys_found),
            style = MTheme.type.highlightTextS.copy(fontSize = 16.sp),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
@Previews
fun JourneysEmptyStatePreview() {
    ResaTheme {
        JourneysEmptyState(
            modifier = Modifier.background(Color.White),
        )
    }
}