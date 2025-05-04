package com.mazarini.resa.ui.screens.home.components

import LocaleHelper
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.BuildConfig
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.capitalizeFirst
import com.mazarini.resa.ui.commoncomponents.dialogs.RadioOption
import com.mazarini.resa.ui.screens.home.state.HomeUiEvent
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import kotlinx.coroutines.launch

@Composable
fun LanguageDialog(
    currentLanguage: String,
    onEvent: (HomeUiEvent) -> Unit,
    onDismiss: () -> Unit,
) {
    val selectedLanguageCode = remember { mutableStateOf(
        BuildConfig.TRANSLATED_LOCALES.firstOrNull {
            it == currentLanguage
        } ?: "en"
    ) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val languages = remember { LocaleHelper.availableLanguages() }

    AlertDialog(
        containerColor = MTheme.colors.surface,
        titleContentColor = MTheme.colors.textPrimary,
        iconContentColor = MTheme.colors.primary,
        icon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.ic_language),
                contentDescription = null,
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.selected),
                style = MTheme.type.highlightTitle,
            )
        },
        text = {
            Column(modifier = Modifier) {
                languages.forEach { language ->
                    RadioOption(
                        name = language.value.capitalizeFirst(),
                        selected = selectedLanguageCode.value == language.key,
                        onClick = {
                            selectedLanguageCode.value = language.key
                        }
                    )
                }
                Text(
                    text = stringResource(R.string.translation_disclaimer),
                    style = MTheme.type.secondaryText,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        onEvent(HomeUiEvent.SetLanguage(selectedLanguageCode.value))
                        LocaleHelper.getNewLocaleContext(context, selectedLanguageCode.value)
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
}

@Composable
@Preview
fun LanguageDialogPreview() {
    ResaTheme {
        LanguageDialog(
            onEvent = { /*TODO*/ },
            currentLanguage = "en",
        ) { /*TODO*/ }
    }
}
