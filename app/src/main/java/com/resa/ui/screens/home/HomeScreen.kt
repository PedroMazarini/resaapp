package com.resa.ui.screens.home

import android.annotation.SuppressLint
import android.content.res.Resources.getSystem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.resa.R
import com.resa.global.fake.FakeFactory
import com.resa.ui.commoncomponents.SavedJourneyItem
import com.resa.ui.screens.home.components.bars.HomeSearchBar
import com.resa.ui.screens.home.state.HomeUiState
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.Previews
import com.resa.ui.util.fontSize
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()
val Int.density: Int get() = (this / getSystem().displayMetrics.density).toInt()

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onFavClicked: (favoriteId: String) -> Unit = {},
    navToLocationSearch: () -> Unit = {},
    onRecentSearchClicked: (searchId: String) -> Unit = {},
) {

    val lazyListState = rememberLazyListState()

    val searchBarOffset = calculateSearchOffeset(lazyListState)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MTheme.colors.background),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .graphicsLayer {
                    translationY = searchBarOffset
                }
                .background(MTheme.colors.background)
                .padding(bottom = 4.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {

            GlideImage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 28.dp),
                imageModel = { "https://snazzy-maps-cdn.azureedge.net/assets/151-ultra-light-with-labels.png?v=20170626083737" },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                ),
                requestBuilder = {
                    Glide
                        .with(LocalContext.current)
                        .asBitmap()
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                },
                previewPlaceholder = R.drawable.map_preview,
            )
            HomeSearchBar(
                onSearchBarClicked = navToLocationSearch,
            )
        }

        Box {
            SavedJourneys(uiState)
        }
    }

}

@Composable
fun SavedJourneys(uiState: HomeUiState) {
    val savedJourneys by uiState.savedJourneys

    Column {
        Text(
            modifier = Modifier
                .padding(start = 24.dp)
                .padding(vertical = 16.dp),
            text = stringResource(id = R.string.saved),
            style = MTheme.type.secondaryText.fontSize(16.sp),
        )

        LazyRow(
            modifier = Modifier,
            contentPadding = PaddingValues(start = 24.dp),
            horizontalArrangement = spacedBy(8.dp),
        ) {
            items(savedJourneys) {
                SavedJourneyItem(
                    journeySearch = it,
                    showDeleteButton = true,
                )
            }
        }
    }
}

@Composable
fun calculateSearchOffeset(lazyListState: LazyListState): Float {
    val offsetChange by remember {
        derivedStateOf {
            (lazyListState.firstVisibleItemIndex == 0) &&
                    (lazyListState.firstVisibleItemScrollOffset.density < 106)
        }
    }
    return if (offsetChange)
        lazyListState.firstVisibleItemScrollOffset.toFloat().unaryMinus()
    else 106.px.toFloat().unaryMinus()
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Previews
fun HomeDefaultPreview() {
    ResaTheme {
        HomeScreen(
            uiState = HomeUiState(
                savedJourneys = mutableStateOf(FakeFactory.journeySearchList()),
            ),
        )
    }
}
