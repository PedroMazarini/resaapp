package com.mazarini.resa.ui.screens.departures.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.domain.model.stoparea.StopJourney
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.commoncomponents.LegBoxIcon
import com.mazarini.resa.ui.screens.departures.state.DeparturesUiEvent
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews
import com.mazarini.resa.ui.util.departMilliAsText
import com.mazarini.resa.ui.util.departMilliToStyle
import com.mazarini.resa.ui.util.fontSize
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicatorDefaults
import eu.bambooapps.material3.pullrefresh.PullRefreshState
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeparturesResultList(
    departures: List<StopJourney>,
    onEvent: (DeparturesUiEvent) -> Unit,
) {
    val isRefreshing by remember {
        mutableStateOf(false)
    }
    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        onEvent(DeparturesUiEvent.PullRefresh)
    })

    Box {
        if (departures.isNotEmpty()) {
            DeparturesList(departures, pullRefreshState)

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                scale = true,
                colors = PullRefreshIndicatorDefaults.colors(
                    containerColor = MTheme.colors.primary,
                    contentColor = MTheme.colors.background,
                ),
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
        } else {
            DeparturesEmptyState(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeparturesList(
    stopJourneys: List<StopJourney>,
    pullRefreshState: PullRefreshState,
) {

    LazyColumn(
        modifier = Modifier.pullRefresh(pullRefreshState),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        stopJourneys.groupBy { it.platform }.onEach { item ->
            item {
                DeparturesPlatformCard(item.value)
            }
        }
    }
}

@Composable
fun RowScope.TextItem(
    text: String
) {
    Text(
        text = text,
        Modifier
            .weight(1f)
            .padding(8.dp),
        style = MTheme.type.secondaryText.fontSize(12.sp),
        maxLines = 4,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun NextIn(
    timeMillis: Long,
) {
    val text = departMilliAsText(departInMilli = timeMillis)
    val style = departMilliToStyle(departInMilli = timeMillis)
    Text(
        modifier = Modifier.padding(8.dp),
        text = text,
        style = style,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun LineBox(
    stopJourney: StopJourney,
) {
    Box(
        Modifier
            .padding(horizontal = 8.dp)
            .padding(vertical = 4.dp),
    ) {
        LegBoxIcon(
            legName = stopJourney.shortName,
            legColors = stopJourney.colors,
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Previews
fun GetDeparturesAroundUseCasePreview() {
    ResaTheme {
        DeparturesResultList(
            departures = FakeFactory.stopJourneyList(),
            onEvent = {},
        )
    }
}
