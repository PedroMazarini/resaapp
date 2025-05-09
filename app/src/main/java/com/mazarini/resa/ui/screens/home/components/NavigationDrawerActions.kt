package com.mazarini.resa.ui.screens.home.components

import androidx.compose.runtime.Composable
import com.mazarini.resa.global.model.ThemeSettings
import com.mazarini.resa.ui.commoncomponents.dialogs.ContactDialog
import com.mazarini.resa.ui.commoncomponents.dialogs.ThemeDialog
import com.mazarini.resa.ui.commoncomponents.dialogs.TicketsDialog
import com.mazarini.resa.ui.screens.home.state.HomeUiEvent

@Composable
fun NavigationDrawerAction(
    actionType: NavigationActionType,
    currentLanguage: String,
    currentTheme: ThemeSettings,
    onEvent: (HomeUiEvent) -> Unit,
    clearAction: () -> Unit,
) {

    when (actionType) {
        NavigationActionType.THEME -> {
            ThemeDialog (
                currentTheme = currentTheme,
                onResult = { theme -> onEvent(HomeUiEvent.OnThemeChanged(theme)) },
                onDismiss = { clearAction() }
            )
        }
        NavigationActionType.TICKETS -> {
            TicketsDialog {
                clearAction()
            }
        }
        NavigationActionType.CONTACT -> {
            ContactDialog {
                clearAction()
            }
        }
        NavigationActionType.LANGUAGE -> {
            LanguageDialog(currentLanguage = currentLanguage, onEvent = onEvent) {
                clearAction()
            }
        }
        NavigationActionType.NONE -> {

        }
    }
}

enum class NavigationActionType {
    NONE,
    TICKETS,
    THEME,
    LANGUAGE,
    CONTACT,
}