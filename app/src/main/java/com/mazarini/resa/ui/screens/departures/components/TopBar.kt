package com.mazarini.resa.ui.screens.departures.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.stringRes
import com.mazarini.resa.ui.navigation.Route
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    navigateTo: (Route) -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        IconButton(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .padding(start = 24.dp)
                .clip(CircleShape)
                .background(MTheme.colors.btnBackground)
                .size(32.dp)
                .align(Alignment.CenterStart),
            onClick = { navigateTo(Route.Back) }
        ) {
            Icon(
                painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                tint = MTheme.colors.textPrimary,
            )
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = R.string.departures.stringRes(),
            style = MTheme.type.highlightTitle.copy(fontSize = 20.sp),
        )
    }
}

@Composable
@Previews
fun TopBarPreview() {
    ResaTheme(darkTheme = isSystemInDarkTheme()) {
        TopBar()
    }
}