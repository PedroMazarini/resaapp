package com.mazarini.resa.ui.screens.journeyselection.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mazarini.resa.R
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme

@Composable
fun TripSearchLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {

        Text(
            text = stringResource(id = R.string.loading),
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally),
            style = MTheme.type.highlightTitleS,
        )
    }
}

@Composable
@Preview
fun TripSearchLoadingPreview() {
    ResaTheme {
        TripSearchLoading()
    }
}
