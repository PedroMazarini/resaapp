package com.mazarini.resa.ui.screens.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mazarini.resa.ui.screens.onboarding.ONBOARDING_PAGES_COUNT
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme

@Composable
fun PageIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(ONBOARDING_PAGES_COUNT) { index ->
            IndicatorItem(
                modifier = Modifier.weight(1f),
                isSelected = pagerState.currentPage >= index,
            )
        }
    }
}

@Composable
fun IndicatorItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
) {
    val color = if (isSelected) MTheme.colors.graph.normal else MTheme.colors.graph.detail
    Box(
        modifier = modifier
            .height(6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color)
    ) {}
}

@Composable
@Preview
fun PageIndicatorPreview() {
    ResaTheme {
        PageIndicator(pagerState = rememberPagerState { 5 })
    }
}
