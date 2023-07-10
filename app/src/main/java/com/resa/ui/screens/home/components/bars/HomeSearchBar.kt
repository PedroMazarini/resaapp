package com.resa.ui.screens.home.components.bars

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resa.ui.theme.MTheme
import com.resa.R
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.Previews

@Composable
fun HomeSearchBar(
    modifier: Modifier = Modifier,
    onSearchBarClicked: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onSearchBarClicked() },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MTheme.colors.background,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.where_to_question),
                maxLines = 1,
                modifier = Modifier.padding(
                    start = 24.dp,
                ),
                style = MTheme.type.fadedTextS,
                fontSize = 16.sp,
            )
            Card(
                shape = RoundedCornerShape(4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterVertically)
                    .padding(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MTheme.colors.primary,
                ),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Center,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(20.dp)
                            .align(Center)
                    )
                }
            }
        }
    }
}

@Composable
@Previews
fun HomeSearchBarPreview() {
    ResaTheme {
        HomeSearchBar {}
    }
}