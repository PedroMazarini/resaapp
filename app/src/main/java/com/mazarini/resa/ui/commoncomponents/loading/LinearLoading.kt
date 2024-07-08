package com.mazarini.resa.ui.commoncomponents.loading

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mazarini.resa.ui.theme.MTheme

@Composable
fun LinearLoading(
    modifier: Modifier = Modifier,
) {
    LinearProgressIndicator(
        modifier = modifier
            .height(3.dp)
            .fillMaxWidth(),
        color = MTheme.colors.primary,
        trackColor = MTheme.colors.background,
    )
}