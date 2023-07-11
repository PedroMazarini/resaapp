package com.resa.ui.screens.home

import android.content.res.Resources.getSystem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.resa.R
import com.resa.ui.screens.home.components.bars.HomeSearchBar
import com.resa.ui.screens.home.state.HomeUiState
import com.resa.ui.screens.journeyselection.component.ShimmerJourneyItem
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.Previews
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()
val Int.density: Int get() = (this / getSystem().displayMetrics.density).toInt()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    onFavClicked: (favoriteId: String) -> Unit = {},
    navToLocationSearch: () -> Unit = {},
    onRecentSearchClicked: (searchId: String) -> Unit = {},
) {

    val lazyListState = rememberLazyListState()

    val searchBarOffset = calculateSearchOffeset(lazyListState)


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

@Composable
@Previews
fun HomeDefaultPreview() {
    ResaTheme {
        HomeScreen(
            homeUiState = HomeUiState(),
        )
    }
}
