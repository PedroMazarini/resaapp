package com.resa.ui.screens.home.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resa.R
import com.resa.domain.model.stoparea.StopJourney
import com.resa.domain.model.stoparea.StopPoint
import com.resa.global.extensions.asCardElevation
import com.resa.global.fake.FakeFactory
import com.resa.ui.commoncomponents.LegBoxIcon
import com.resa.ui.screens.home.model.StopPointsState
import com.resa.ui.screens.home.state.HomeUiEvent
import com.resa.ui.screens.home.state.HomeUiState
import com.resa.ui.screens.home.state.HomeViewModel.PendingLocationUse
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.departMilliAsText
import com.resa.ui.util.departMilliToStyle
import com.resa.ui.util.Previews
import com.resa.ui.util.asSpanStyle
import com.resa.ui.util.color
import com.resa.ui.util.diffFromNow
import com.resa.ui.util.firstBold
import com.resa.ui.util.fontSize
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicatorDefaults
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Departures(
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
) {

    val stopPointsState by homeUiState.stopPoints.collectAsState()
    val isRefreshing by remember {
        mutableStateOf(false)
    }
    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        onEvent(HomeUiEvent.RefreshDepartures)
    })

    Column {
        DeparturesHeader(homeUiState)
        Card(
            modifier = modifier.padding(horizontal = 4.dp),
            shape = RoundedCornerShape(2.dp),
            elevation = 1.dp.asCardElevation(),
            colors = CardDefaults.cardColors(
                containerColor = MTheme.colors.background,
            )
        ) {

            Box {
                when (val state = stopPointsState) {
                    is StopPointsState.Error -> NoDeparturesFound(
                        modifier = Modifier.fillMaxSize(),
                        onRetry = { onEvent(HomeUiEvent.RefreshDepartures) }
                    )

                    is StopPointsState.Loaded -> {
                        LazyColumn(
                            Modifier
                                .pullRefresh(pullRefreshState)
                                .fillMaxSize()
                        ) {
                            departuresLoaded(state.stopPoints)
                        }
                    }

                    StopPointsState.Loading -> LoadingDepartures()
                    StopPointsState.NeedLocation ->
                        NeedLocationDepartures(modifier = Modifier.fillMaxSize()) {
                            onEvent(
                                HomeUiEvent.UpdateGpsRequest(
                                    true,
                                    pendingLocationUse = PendingLocationUse.SEARCH_DEPARTURES_AROUND,
                                )
                            )
                        }
                }
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
            }
        }
    }
}

@Composable
fun DeparturesHeader(uiState: HomeUiState) {

    val isDeparturesReloading by remember { uiState.isDeparturesReloading }
    Row {
        Text(
            modifier = Modifier
                .padding(start = 24.dp)
                .padding(top = 16.dp),
            text = stringResource(id = R.string.departures),
            style = MTheme.type.secondaryText.fontSize(16.sp),
        )
        if (isDeparturesReloading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .padding(top = 16.dp)
                    .size(18.dp),
                strokeWidth = 2.dp,
                color = MTheme.colors.primary,
                trackColor = MTheme.colors.background,
            )
        }
    }
}

private fun LazyListScope.departuresLoaded(stopPoints: List<StopPoint>) {

    stopPoints
        .forEachIndexed { stopIndex, stopPoint ->

            if (stopPoint.departures.isEmpty()) return@forEachIndexed

            tableHeader(
                modifier = if (stopIndex != 0) Modifier.padding(top = 8.dp) else Modifier,
                text = "${stopPoint.platform} - ${stopPoint.name}"
            )

            itemsIndexed(stopPoint.departures) { index, departure ->
                val isLast = index == stopPoint.departures.lastIndex
                val departInMilli = remember { mutableLongStateOf(departure.time.diffFromNow()) }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LineBox(stopJourney = departure)
                    Direction(weight = 1f, direction = departure.direction)
                    NextIn(departInMilli.longValue)
                }

                if (!isLast) {
                    Divider(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        color = MTheme.colors.primary,
                        thickness = 1.dp,
                    )
                }

                LaunchedEffect(key1 = departInMilli) {
                    while (true) {
                        delay(10000)
                        departInMilli.longValue = departure.time.diffFromNow()
                    }
                }
            }
        }

}

fun LazyListScope.tableHeader(
    modifier: Modifier = Modifier,
    text: String
) {
    item {
        Row(
            modifier
                .background(color = MTheme.colors.primary)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = text.firstBold(
                    style = MTheme.type.secondaryText
                        .fontSize(12.sp)
                        .color(Color.White)
                        .asSpanStyle()
                ),
                Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            )

            Text(
                text = stringResource(id = R.string.next_in).lowercase(),
                Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                style = MTheme.type.secondaryText
                    .fontSize(12.sp)
                    .color(Color.White),
            )
        }
    }
}

@Composable
fun RowScope.Direction(
    weight: Float,
    direction: String
) {
    Text(
        text = direction,
        Modifier
            .weight(weight)
            .padding(8.dp),
        style = MTheme.type.secondaryText.fontSize(12.sp),
        maxLines = 4,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun NextIn(timeMillis: Long) {
    val text = departMilliAsText(departInMilli = timeMillis)
    val style = departMilliToStyle(departInMilli = timeMillis)
    Text(
        text = text,
        Modifier.padding(8.dp),
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
            .padding(start = 8.dp)
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
        Departures(
            modifier = Modifier
                .fillMaxSize(),
            homeUiState = HomeUiState(
                stopPoints = MutableStateFlow(
                    StopPointsState.Loaded(
                        stopPoints = FakeFactory.stopPointList()
                    )
                ),
            ),
            onEvent = {},
        )
    }
}
