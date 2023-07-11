package com.resa.ui.commoncomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.resa.R
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.theme.colors.DarkLemon
import com.resa.ui.util.Previews

@Composable
fun LiveIcon(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(
                color = MTheme.colors.primary,
                shape = RoundedCornerShape(2.dp),
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .padding(start = 4.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(color = MTheme.colors.background),
        )
        Text(
            modifier = Modifier
                .padding(start = 8.dp, end = 4.dp),
            text = stringResource(R.string.live),
            style = MTheme.type.secondaryText.copy(
                color = MTheme.colors.background
            ),
        )
    }
}

@Previews
@Composable
fun LiveIconPreview() {
    ResaTheme {
        Surface(
            modifier = Modifier.background(color = MTheme.colors.background)
        ) {
            LiveIcon()
        }
    }
}