package com.mazarini.resa.ui.screens.locationsearch.components.searchFields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.isNotNull
import com.mazarini.resa.ui.screens.locationsearch.state.CurrentLocation
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun CurrentLocationButton(
    modifier: Modifier = Modifier,
    currentLocation: CurrentLocation?,
) {

    Row(
        modifier = modifier
            .height(64.dp)
            .fillMaxWidth()
            .background(MTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(80.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            if (currentLocation.isNotNull && currentLocation is CurrentLocation.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MTheme.colors.primary,
                    trackColor = MTheme.colors.background,
                )
            } else {
                Icon(
                    modifier = Modifier.padding(20.dp),
                    painter = painterResource(id = R.drawable.ic_crosshair),
                    contentDescription = null,
                    tint = MTheme.colors.textPrimary,
                )
            }
        }
        Text(
            text = stringResource(R.string.use_my_location),
            style = MTheme.type.textField,
        )
    }
}

@Composable
@Previews
fun CurrentLocationButtonPreview() {
    ResaTheme {
        CurrentLocationButton(
            currentLocation = null,
        )
    }
}
