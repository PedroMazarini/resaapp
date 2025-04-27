package com.mazarini.resa.ui.screens.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.ui.navigation.Route
import com.mazarini.resa.ui.screens.journeydetails.components.journeydetailedlegs.legdetails.PageIndicator
import com.mazarini.resa.ui.screens.onboarding.components.DeparturesItem
import com.mazarini.resa.ui.screens.onboarding.components.DetailsItem
import com.mazarini.resa.ui.screens.onboarding.components.FiltersItem
import com.mazarini.resa.ui.screens.onboarding.components.HomeItem
import com.mazarini.resa.ui.screens.onboarding.components.JourneyItem
import com.mazarini.resa.ui.screens.onboarding.components.SearchItem
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import kotlinx.coroutines.launch

const val ONBOARDING_PAGES_COUNT = 6

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    navigateTo: (route: Route) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        val pagerState = rememberPagerState { ONBOARDING_PAGES_COUNT }
        val (buttonText, buttonTextStyle) = if (pagerState.currentPage == ONBOARDING_PAGES_COUNT - 1) {
            stringResource(R.string.get_started) to
            MTheme.type.textField.copy(color = MTheme.colors.primary, fontSize = 22.sp)
        } else {
            stringResource(R.string.dismiss) to
            MTheme.type.textField
        }

        PageIndicator(
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 24.dp),
            pagerState = pagerState,
        )
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState,
        ) {page ->
            Box {
                when(page) {
                    0 -> {
                        HomeItem()
                    }
                    1 -> {
                        DeparturesItem()
                    }
                    2 -> {
                        SearchItem()
                    }
                    3 -> {
                        FiltersItem()
                    }
                    4 -> {
                        JourneyItem()
                    }
                    5 -> {
                        DetailsItem()
                    }
                }
                Box(modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(.5f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(page - 1)
                        }
                    }
                ) {}
                Box(modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(.5f)
                    .align(Alignment.TopEnd)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        if (page == ONBOARDING_PAGES_COUNT - 1) {
                            navigateTo(Route.Back)
                        } else {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page + 1)
                            }
                        }
                    }
                ) {}
            }
        }

        TextButton(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            onClick = { navigateTo(Route.Back) }
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = buttonText,
                style = buttonTextStyle,
            )
        }
    }
}

@Composable
@Preview
fun OnboardingScreenPreview() {
    ResaTheme {
        OnboardingScreen()
    }
}
