package com.mazarini.resa.ui.screens.journeydetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mazarini.resa.ui.screens.journeyselection.component.shimmerEffect
import com.mazarini.resa.ui.theme.ResaTheme

@Composable
fun LoadingJourneyDetails() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        repeat(2) {
            Row(modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .height(20.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(20.dp)
                        .shimmerEffect()
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(top = 32.dp)
                .height(1.dp)
                .fillMaxWidth()
                .shimmerEffect()
        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .size(15.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(8.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(15.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(8.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(15.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(8.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(15.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(8.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(15.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(8.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(15.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(8.dp)
                    .shimmerEffect()
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .height(1.dp)
                .fillMaxWidth()
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .padding(top = 28.dp)
                .fillMaxWidth()
                .aspectRatio(4f)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        bottomStart = 12.dp,
                        topEnd = 12.dp,
                        bottomEnd = 12.dp
                    )
                )
                .shimmerEffect(),
        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(10.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column (
                modifier = Modifier
                    .fillMaxWidth(0.6f),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .shimmerEffect()
                )
            }
        }
        repeat(5) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(top = 16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(10.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(.7f)
                                .height(10.dp)
                                .shimmerEffect()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(.5f)
                                .height(10.dp)
                                .shimmerEffect()
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .height(1.dp)
                            .fillMaxWidth()
                            .shimmerEffect()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(.7f)
                                .height(10.dp)
                                .shimmerEffect()
                        )
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .shimmerEffect()
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .height(1.dp)
                            .fillMaxWidth()
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun LoadingJourneyDetailsPreview() {
    ResaTheme {
        LoadingJourneyDetails()
    }
}