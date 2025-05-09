package com.mazarini.resa.ui.screens.home.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.global.model.ThemeSettings
import com.mazarini.resa.ui.navigation.Route
import com.mazarini.resa.ui.screens.home.state.HomeUiEvent
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import kotlinx.coroutines.launch

@Composable
fun BoxScope.NavigationDrawer(
    modifier: Modifier,
    yOffsetDp: Dp,
    currentLanguage: String,
    currentTheme: ThemeSettings,
    onEvent: (HomeUiEvent) -> Unit,
    navigateTo: (route: Route) -> Unit = {},
    content: @Composable () -> Unit,
) {
    val drawerState: DrawerState = remember { DrawerState(DrawerValue.Closed) }
    val action = remember { mutableStateOf(NavigationActionType.NONE) }
    val coroutineScope = rememberCoroutineScope()
    val currentDirection = LocalLayoutDirection.current
    val context = LocalContext.current
    val version =
        context.packageManager.getPackageInfo("com.mazarini.resa", 0).versionName.orEmpty()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            modifier = modifier,
            drawerState = drawerState,
            scrimColor = MTheme.colors.surface.copy(alpha = 0.5f),
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .shadow(elevation = 16.dp),
                    drawerContainerColor = MTheme.colors.surface,
                    drawerTonalElevation = 16.dp,
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides currentDirection) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.End)
                                .fillMaxWidth()
                                .padding(top = 42.dp),
                        ) {
                            MenuItem(
                                title = stringResource(id = R.string.departures),
                                icon = painterResource(id = R.drawable.ic_calendar),
                                onClick = { navigateTo(Route.Departures) },
                            )
                            MenuItem(
                                title = stringResource(id = R.string.tickets),
                                icon = painterResource(id = R.drawable.ic_ticket),
                                onClick = { action.value = NavigationActionType.TICKETS },
                            )
                            MenuItem(
                                title = stringResource(id = R.string.theme),
                                icon = painterResource(id = R.drawable.ic_theme),
                                onClick = { action.value = NavigationActionType.THEME },
                            )
                            MenuItem(
                                title = stringResource(id = R.string.how_it_works),
                                icon = painterResource(id = R.drawable.ic_bulb),
                                onClick = { navigateTo(Route.Onboarding) },
                            )
                            MenuItem(
                                title = stringResource(id = R.string.language),
                                icon = painterResource(id = R.drawable.ic_language),
                                onClick = { action.value = NavigationActionType.LANGUAGE },
                            )
                            MenuItem(
                                title = stringResource(id = R.string.contact_title),
                                icon = painterResource(id = R.drawable.ic_phone),
                                onClick = { action.value = NavigationActionType.CONTACT },
                            )
                            MenuItem(
                                title = stringResource(id = R.string.about),
                                icon = painterResource(id = R.drawable.ic_info),
                                onClick = { navigateTo(Route.About) },
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                                text = "v$version",
                                style = MTheme.type.secondaryText
                            )
                        }

                    }
                }
            }) {
            CompositionLocalProvider(LocalLayoutDirection provides currentDirection) {
                content()
            }
        }
    }

    IconButton(
        modifier = Modifier
            .padding(16.dp)
            .offset(y = yOffsetDp)
            .align(Alignment.TopEnd),
        onClick = {
            coroutineScope.launch {
                if (drawerState.isOpen) {
                    drawerState.close()
                } else {
                    drawerState.open()
                }
            }
        },
    ) {
        val icon = if (drawerState.isOpen) R.drawable.ic_arrow_forward else R.drawable.ic_menu

        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = MTheme.colors.primary,
        )
    }

    NavigationDrawerAction(
        actionType = action.value,
        currentLanguage = currentLanguage,
        currentTheme = currentTheme,
        onEvent = onEvent,
    ) {
        action.value = NavigationActionType.NONE
    }

    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch {
            drawerState.close()
        }
    }
}

@Composable
fun MenuItem(
    title: String,
    icon: Painter,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterVertically),
            painter = icon,
            contentDescription = null,
            tint = MTheme.colors.primary,
        )
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically),
            text = title,
            style = MTheme.type.textField.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
            ),
        )
    }
}

@Composable
@Preview
fun NavigationDrawerPreview() {
    ResaTheme {
        Box {
            NavigationDrawer(
                modifier = Modifier,
                yOffsetDp = 0.dp,
                currentLanguage = "en",
                currentTheme = ThemeSettings.SYSTEM,
                onEvent = {},
                content = {},
            )
        }
    }
}