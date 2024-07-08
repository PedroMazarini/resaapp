package com.mazarini.resa.ui.screens.home.components

import androidx.compose.runtime.Composable
import com.mazarini.resa.ui.commoncomponents.dialogs.ContactDialog
import com.mazarini.resa.ui.commoncomponents.dialogs.LanguageDialog
import com.mazarini.resa.ui.commoncomponents.dialogs.ThemeDialog
import com.mazarini.resa.ui.commoncomponents.dialogs.TicketsDialog
import com.mazarini.resa.ui.screens.home.state.HomeUiEvent
import com.mazarini.resa.ui.screens.home.state.HomeUiState

@Composable
fun NavigationDrawerAction(
    actionType: NavigationActionType,
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    clearAction: () -> Unit,
) {

    when (actionType) {
        NavigationActionType.THEME -> {
            ThemeDialog { clearAction() }
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
            LanguageDialog {
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