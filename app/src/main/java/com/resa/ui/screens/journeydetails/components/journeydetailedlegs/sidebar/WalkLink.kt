package com.resa.ui.screens.journeydetails.components.journeydetailedlegs.sidebar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resa.R
import com.resa.domain.model.journey.Leg
import com.resa.global.fake.FakeFactory
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme

@Composable
fun WalkLink(leg: Leg) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        when (leg) {
            is Leg.DepartureLink -> DepartureWalk()
            is Leg.ArrivalLink -> ArrivalWalk()
            else -> AccessWalk()
        }
    }
}

@Composable
fun ColumnScope.AccessWalk() {
    WhiteCircleIcon()
    repeat(3) { CircleIcon() }
    WalkIcon()
    repeat(4) { CircleIcon() }
}

@Composable
fun ColumnScope.DepartureWalk() {
    DepartureCircleIcon()
    repeat(3) { CircleIcon() }
    WalkIcon()
    repeat(4) { CircleIcon() }
}

@Composable
fun ColumnScope.ArrivalWalk() {
    WhiteCircleIcon()
    repeat(3) { CircleIcon() }
    WalkIcon()
    repeat(3) { CircleIcon() }
    Image(
        modifier = Modifier
            .size(20.dp)
            .align(Alignment.CenterHorizontally),
        painter = painterResource(id = R.drawable.ic_destination),
        contentDescription = null,
    )
}

@Composable
fun ColumnScope.WalkIcon() {
    Icon(
        modifier = Modifier
            .size(height = 17.dp, width = 12.dp)
            .align(Alignment.CenterHorizontally),
        painter = painterResource(
            id = R.drawable.ic_walk
        ),
        contentDescription = null,
        tint = MTheme.colors.textSecondary
    )
}

@Composable
fun ColumnScope.WhiteCircleIcon() {
    Icon(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .size(8.dp),
        painter = painterResource(
            id = R.drawable.ic_circle,
        ),
        tint = MTheme.colors.background,
        contentDescription = null,
    )
}

@Composable
fun ColumnScope.CircleIcon() {
    Icon(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .size(6.dp),
        painter = painterResource(
            id = R.drawable.ic_circle,
        ),
        tint = MTheme.colors.lightDetail,
        contentDescription = null,
    )
}

@Composable
fun DepartureCircleIcon() {
    Box(
        modifier = Modifier
            .size(12.dp)
            .aspectRatio(1f)
            .background(MTheme.colors.textPrimary, shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .aspectRatio(1f)
                .background(MTheme.colors.background, shape = CircleShape)
                .align(Alignment.Center),
        )
    }
}

@Composable
@Preview
fun WalkLinkPreview() {
    ResaTheme {
        LegsSideBar(
            modifier = Modifier
                .height(130.dp)
                .background(MTheme.colors.background),
            leg = FakeFactory.departWalkLeg()
        )
    }
}

@Composable
@Preview
fun WalkLinkAccessPreview() {
    ResaTheme {
        LegsSideBar(
            modifier = Modifier
                .height(130.dp)
                .background(MTheme.colors.background),
            leg = FakeFactory.accessWalkLeg()
        )
    }
}

@Composable
@Preview
fun WalkLinkArrivalPreview() {
    ResaTheme {
        LegsSideBar(
            modifier = Modifier
                .height(130.dp)
                .background(MTheme.colors.background),
            leg = FakeFactory.arrivalWalkLeg()
        )
    }
}