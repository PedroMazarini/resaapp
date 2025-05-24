package com.mazarini.resa.ui.commoncomponents.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.global.model.ThemeSettings
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme

@Composable
fun ThemeDialog(
    currentTheme: ThemeSettings,
    onResult: (ThemeSettings) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedTheme by remember { mutableStateOf(currentTheme) }

    AlertDialog(
        containerColor = MTheme.colors.surface,
        titleContentColor = MTheme.colors.textPrimary,
        iconContentColor = MTheme.colors.primary,
        icon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.ic_theme),
                contentDescription = null,
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.choose_theme),
                style = MTheme.type.highlightTitle,
            )
        },
        text = {
            Column(modifier = Modifier) {
                RadioOption(
                    name = stringResource(id = ThemeSettings.LIGHT.stringRes()),
                    selected = selectedTheme == ThemeSettings.LIGHT,
                    onClick = { selectedTheme = ThemeSettings.LIGHT }
                )
                RadioOption(
                    name = stringResource(ThemeSettings.DARK.stringRes()),
                    selected = selectedTheme == ThemeSettings.DARK,
                    onClick = { selectedTheme = ThemeSettings.DARK }
                )
                RadioOption(
                    name = stringResource(ThemeSettings.SYSTEM.stringRes()),
                    selected = selectedTheme == ThemeSettings.SYSTEM,
                    onClick = { selectedTheme = ThemeSettings.SYSTEM }
                )
            }
        },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = {
                    onResult(selectedTheme)
                    onDismiss()
                }
            ) {
                Text(
                    text = stringResource(R.string.confirm),
                    style = MTheme.type.secondaryText.copy(fontSize = 16.sp),
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(
                    text = stringResource(R.string.dismiss),
                    style = MTheme.type.secondaryText.copy(fontSize = 16.sp),
                )
            }
        }
    )
}

@Composable
@Preview
fun ThemeDialogPreview() {
    ResaTheme {
        ThemeDialog(
            currentTheme = ThemeSettings.SYSTEM,
            onResult = { /*TODO*/ },
            onDismiss = { /*TODO*/ },
        )
    }
}
