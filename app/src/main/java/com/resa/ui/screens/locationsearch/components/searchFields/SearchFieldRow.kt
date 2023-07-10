package com.resa.ui.screens.locationsearch.components.searchFields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.resa.R
import com.resa.ui.commoncomponents.Animation
import com.resa.ui.commoncomponents.CustomTextField
import com.resa.ui.commoncomponents.LottieAnim
import com.resa.ui.theme.MTheme

@Composable
fun SearchFieldRow(
    modifier: Modifier,
    icon: Painter,
    iconModifier: Modifier,
    textContent: String,
    placeHolder: String,
    focusRequester: FocusRequester,
    textStyle: TextStyle = MTheme.type.textField,
    onFocusChanged: (Boolean) -> Unit,
    onCloseClicked: () -> Unit,
    onTextChanged: (String) -> Unit = {},
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .width(80.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = iconModifier.align(Alignment.Center),
                painter = icon,
                contentDescription = null,
                tint = MTheme.colors.textSecondary,
            )
        }
        CustomTextField(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .onFocusChanged { onFocusChanged(it.hasFocus) }
                .focusRequester(focusRequester),
            value = textContent,
            textStyle = textStyle,
            onValueChange = { onTextChanged(it) },
            placeHolder = placeHolder,
        )
        Box(
            modifier = Modifier
                .width(56.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {

            if (textContent.isNotEmpty()) {
                IconButton(
                    onClick = { onCloseClicked() },
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .clip(CircleShape)
                        .background(MTheme.colors.btnBackground)
                        .size(24.dp)
                ) {
                    Icon(
                        tint = MTheme.colors.textSecondary,
                        modifier = Modifier.size(
                            if (textContent.isEmpty()) 16.dp
                            else 8.dp
                        ),
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}
