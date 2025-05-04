package com.mazarini.resa.ui.screens.locationsearch.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.asCardBackground
import com.mazarini.resa.global.extensions.asCardElevation
import com.mazarini.resa.ui.model.Location
import com.mazarini.resa.ui.model.LocationType
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun LocationItem(
    location: Location,
    highlight: String = "",
    isSaved: Boolean = false,
    onLocationSelected: (location: Location) -> Unit = {},
    saveLocation: (location: Location) -> Unit = {},
    deleteLocation: (id: String) -> Unit = {},
) {

    Row(
        modifier = Modifier.height(48.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .testTag("LocationItem_${location.id}")
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { onLocationSelected(location) },
                    indication = rememberRipple(bounded = true, color = MTheme.colors.secondary)
                )
                .weight(1f),

            ) {
            Card(
                modifier = Modifier
                    .padding(start = 24.dp)
                    .padding(vertical = 8.dp)
                    .size(32.dp)
                    .align(Alignment.CenterVertically),
                shape = CircleShape,
                colors = MTheme.colors.iconBackground.asCardBackground(),
                elevation = 0.dp.asCardElevation(),
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    painter = painterResource(id = location.type.iconResource()),
                    contentDescription = null,
                    tint = MTheme.colors.icon,
                )
            }
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(start = 16.dp)
            ) {
                LocationName(location = location, search = highlight)
            }
        }

        IconButton(
            onClick = {
                if (isSaved) deleteLocation(location.id)
                else saveLocation(location)
            },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .testTag("LocationItemSaveDelete_${location.id}")
                .padding(end = 24.dp)
                .clip(CircleShape)
                .background(MTheme.colors.btnBackground)
                .size(38.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(26.dp),
                painter = getIsSavedIcon(isSaved),
                contentDescription = null,
                tint = MTheme.colors.textPrimary,
            )
        }
    }
}

@Composable
fun getIsSavedIcon(isSaved: Boolean): Painter =
    if (isSaved) {
        painterResource(id = R.drawable.ic_push_pin_filled)
    } else {
        painterResource(id = R.drawable.ic_push_pin_outline)
    }

@Composable
fun LocationName(
    modifier: Modifier = Modifier,
    location: Location,
    search: String,
) {
    if (search.isEmpty()) {
        Text(
            modifier = modifier,
            text = location.name,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            style = MTheme.type.highlightTextS.copy(fontWeight = FontWeight.Normal),
        )
    } else {
        var textResult = AnnotatedString("")
        if (location.name.lowercase().contains(search.lowercase())) {
            val searchIndex = location.name.lowercase().indexOf(search.lowercase())
            textResult = AnnotatedString("").plus(
                textResult.plus(
                    AnnotatedString(
                        text = if (searchIndex > 0) location.name.substring(0, searchIndex) else "",
                        spanStyle = MTheme.type.highlightText.copy(fontWeight = FontWeight.Normal)
                    )
                )
            ).plus(
                AnnotatedString(
                    text = location.name.substring(searchIndex, searchIndex + search.length),
                    spanStyle = MTheme.type.highlightText,
                )
            ).plus(
                AnnotatedString(
                    text =
                    if (location.name.lastIndex == searchIndex + search.length - 1) ""
                    else location.name.substring(
                        searchIndex + search.length,
                        location.name.lastIndex + 1
                    ),
                    spanStyle = MTheme.type.highlightText.copy(fontWeight = FontWeight.Normal)
                )
            )
        }
        if (textResult.isEmpty())
            textResult = AnnotatedString(
                text = location.name,
                spanStyle = MTheme.type.highlightText.copy(fontWeight = FontWeight.Normal),
            )
        Text(
            modifier = modifier,
            text = textResult,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            style = MTheme.type.highlightTextS,
        )

    }
}

@Composable
@Previews
fun LocationItemPreview() {
    ResaTheme {
        Column(
            modifier = Modifier.background(color = MTheme.colors.background)
        ) {
            LocationItem(
                location = Location(
                    id = "1",
                    name = "Location Name",
                    type = LocationType.address,
                ),
                highlight = "Loca",
                onLocationSelected = {},
            )
        }
    }
}
