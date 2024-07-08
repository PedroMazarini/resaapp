package com.mazarini.resa.ui.commoncomponents.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.global.model.ThemeSettings
import com.mazarini.resa.global.preferences.PrefsProvider
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import kotlinx.coroutines.launch

@Composable
fun ThemeDialog(
    onDismiss: () -> Unit,
) {
    val prefsProvider = PrefsProvider(LocalContext.current.applicationContext)
    val currentTheme = remember { mutableStateOf<ThemeSettings?>(null) }
    val coroutineScope = rememberCoroutineScope()

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
                    selected = currentTheme.value == ThemeSettings.LIGHT,
                    onClick = { currentTheme.value = ThemeSettings.LIGHT }
                )
                RadioOption(
                    name = stringResource(ThemeSettings.DARK.stringRes()),
                    selected = currentTheme.value == ThemeSettings.DARK,
                    onClick = { currentTheme.value = ThemeSettings.DARK }
                )
                RadioOption(
                    name = stringResource(ThemeSettings.SYSTEM.stringRes()),
                    selected = currentTheme.value == ThemeSettings.SYSTEM,
                    onClick = { currentTheme.value = ThemeSettings.SYSTEM }
                )
            }
        },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        prefsProvider.setThemeSettings(currentTheme.value!!)
                        onDismiss()
                    }
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
    LaunchedEffect(Unit) {
        currentTheme.value = prefsProvider.getThemeSettings()
    }
}

@Composable
@Preview
fun ThemeDialogPreview() {
    ResaTheme {
        ThemeDialog(
            onDismiss = { /*TODO*/ }
        )
    }
}
