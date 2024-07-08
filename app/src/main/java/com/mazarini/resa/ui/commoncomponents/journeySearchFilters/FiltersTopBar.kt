package com.mazarini.resa.ui.commoncomponents.journeySearchFilters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.asCardBackground
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun FiltersTopBar(
    modifier: Modifier = Modifier,
    iconId: Int,
    filterDetail: String,
    onIconClicked: () -> Unit = {},
    onFilterClicked: () -> Unit = {},
) {
    val background: Color = MTheme.colors.background
    val btnBackground = MTheme.colors.btnBackground
    val contentColor = MTheme.colors.textPrimary

    Surface(
        modifier
            .height(56.dp)
            .fillMaxWidth(),
        color = background,
    ) {
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(
                onClick = { onIconClicked() },
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .padding(start = 24.dp)
                    .clip(CircleShape)
                    .background(btnBackground)
                    .size(32.dp)
            ) {
                Icon(
                    painterResource(id = iconId),
                    contentDescription = null,
                    tint = contentColor,
                )
            }

            Card(
                modifier = Modifier
                    .padding(end = 24.dp)
                    .fillMaxHeight()
                    .padding(vertical = 12.dp)
                    .clickable { onFilterClicked() },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                colors = btnBackground.asCardBackground(),
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .padding(end = 13.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = filterDetail,
                        style = MTheme.type.highlightTitle
                            .copy(
                                color = contentColor,
                                fontSize = 12.sp,
                            ),
                    )
                    Icon(
                        modifier = Modifier
                            .height(24.dp)
                            .padding(start = 4.dp),
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = null,
                        tint = contentColor,
                    )
                }
            }
        }
    }
}

@Composable
private fun getColors(isFilterSheetOpen: Boolean) =
    if (isFilterSheetOpen) {
        Triple(
            MTheme.colors.surfaceBlur,
            MTheme.colors.graph.soft,
            MTheme.colors.graph.active,
        )
    } else {
        Triple(
            MTheme.colors.background,
            MTheme.colors.btnBackground,
            MTheme.colors.textPrimary,
        )
    }

@Composable
@Previews
fun FiltersTopBarPreview() {
    ResaTheme {
        Column(modifier = Modifier.background(Color.White)) {
            FiltersTopBar(
                iconId = R.drawable.ic_back,
                filterDetail = "Depart today at 5:13",
            )
        }
    }
}
