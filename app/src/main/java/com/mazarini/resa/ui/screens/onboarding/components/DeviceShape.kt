package com.mazarini.resa.ui.screens.onboarding.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.asCardBackground
import com.mazarini.resa.ui.screens.onboarding.model.DeviceShapeType
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme

@Composable
fun DeviceShape(
    modifier: Modifier = Modifier,
    deviceShapeType: DeviceShapeType = DeviceShapeType.WHOLE,
    content: @Composable (modifier: Modifier) -> Unit = {},
) {
    when (deviceShapeType) {
        DeviceShapeType.WHOLE -> WholeDeviceShape(modifier = modifier, content = content)
        DeviceShapeType.TOP -> HalfDeviceShape(modifier = modifier, content = content)
    }
}

@Composable
fun WholeDeviceShape(
    modifier: Modifier = Modifier,
    content: @Composable (modifier: Modifier) -> Unit = {},
) {
    Box(modifier = modifier) {
        Card(
            modifier = Modifier
                .padding(5.dp)
                .aspectRatio(.47f)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(36.dp),
            colors = Color.White.asCardBackground(),
            border = BorderStroke(8.dp, MTheme.colors.primary),
        ) {
            Column {
                content(Modifier)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 56.dp)
                        .padding(bottom = 10.dp)
                        .height(22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(10.dp),
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = null,
                        tint = MTheme.colors.primary.copy(alpha = .5f),
                    )
                    Icon(
                        modifier = Modifier.size(10.dp),
                        painter = painterResource(id = R.drawable.ic_square),
                        contentDescription = null,
                        tint = MTheme.colors.primary.copy(alpha = .5f),
                    )
                    Icon(
                        modifier = Modifier.size(10.dp),
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        tint = MTheme.colors.primary.copy(alpha = .5f),
                    )
                }
            }
        }
    }
}

@Composable
fun HalfDeviceShape(
    modifier: Modifier = Modifier,
    content: @Composable (modifier: Modifier) -> Unit = {},
) {
    Box(modifier = modifier
        .wrapContentHeight()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
            colors = Color.White.asCardBackground(),
            border = BorderStroke(10.dp, MTheme.colors.primary),
        ) {
            content(Modifier.fillMaxWidth())
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MTheme.colors.background)
                .align(Alignment.BottomCenter)
                .height(48.dp),
        ){

        }

    }
}

@Composable
@Preview
fun WholeDeviceShapePreview() {
    ResaTheme {
        DeviceShape {
            Text(modifier = it, text = "Test", style = MTheme.type.textField)
        }
    }
}

@Composable
@Preview
fun WholeDeviceShapeHalfPreview() {
    ResaTheme {
        DeviceShape(deviceShapeType = DeviceShapeType.TOP) {
            Text(modifier = it, text = "Test", style = MTheme.type.textField)
        }
    }
}