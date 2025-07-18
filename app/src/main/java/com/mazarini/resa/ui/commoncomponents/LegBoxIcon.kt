package com.mazarini.resa.ui.commoncomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mazarini.resa.domain.model.journey.LegColors
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun LegBoxIcon(
    modifier: Modifier = Modifier,
    legName: String,
    legColors: LegColors,
) {
    val borderSize = if (legColors.hasBorder()) 1.dp else 0.dp
    Card(
        modifier = modifier.height(20.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = legColors.background,
        ),
        border = BorderStroke(borderSize, legColors.foreground),
    ) {
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                text = legName,
                textAlign = TextAlign.Center,
                style = MTheme.type.tertiaryLightText.copy(color = legColors.foreground),
                maxLines = 1,
            )
        }
    }
}

@Composable
@Previews
fun LegBoxIconPreview() {
    ResaTheme {
        LegBoxIcon(
            modifier = Modifier,
            legName = "Röd",
            legColors = LegColors(
                foreground = Color.White,
                background = Color.Red,
                border = Color.Blue,
            ),
        )
    }
}
