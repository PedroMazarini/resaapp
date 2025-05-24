package com.mazarini.resa.ui.commoncomponents.dialogs

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.TransportMode
import com.mazarini.resa.domain.model.TransportMode.Companion.stringRes
import com.mazarini.resa.global.extensions.capitalizeFirst
import com.mazarini.resa.global.extensions.toggle
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.iconResource
import com.mazarini.resa.ui.util.showMessage
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf

@Composable
fun TransportModesDialog(
    onResult: (List<TransportMode>) -> Unit,
    preferredModes: List<TransportMode>,
    onDismiss: () -> Unit,
) {
    var selectedModes by remember { mutableStateOf(preferredModes) }
    val selectableModes = immutableListOf(
        TransportMode.bus,
        TransportMode.tram,
        TransportMode.ferry,
        TransportMode.train
    )
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    AlertDialog(
        containerColor = MTheme.colors.surface,
        titleContentColor = MTheme.colors.textPrimary,
        iconContentColor = MTheme.colors.primary,
        icon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.ic_transport_mode),
                contentDescription = null,
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.select_transport_modes),
                style = MTheme.type.highlightTitle,
            )
        },
        text = {
            Column(modifier = Modifier) {
                selectableModes.forEach { mode ->
                    TransportModeOption(
                        transportMode = mode,
                        checked = selectedModes.contains(mode),
                        onClick = { selectedModes = selectedModes.toggle(mode) }
                    )
                }
            }
        },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        validateAndReturn(
                            context = context,
                            selectedModes,
                            onResult,
                        )
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

fun validateAndReturn(
    context: Context,
    modes: List<TransportMode>,
    onResult: (List<TransportMode>) -> Unit,
) {
    if (modes.isEmpty()) {
        context.showMessage(
            message = context.getString(R.string.select_at_least_one_mode),
        )
    } else {
        onResult(modes)
    }
}

@Composable
fun TransportModeOption(
    transportMode: TransportMode,
    checked: Boolean,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onClick() },
            colors = CheckboxDefaults.colors(
                checkedColor = MTheme.colors.primary,
                uncheckedColor = MTheme.colors.textSecondary,
                checkmarkColor = MTheme.colors.surface,
            ),
        )
        Icon(
            modifier = Modifier
                .size(20.dp)
                .fillMaxSize(),
            painter = painterResource(id = transportMode.iconResource()),
            contentDescription = null,
            tint = MTheme.colors.textSecondary,
        )
        Text(
            text = stringResource(id = transportMode.stringRes()).capitalizeFirst(),
            style = MTheme.type.secondaryText,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
@Preview
fun TransportModesPreview() {
    ResaTheme {
        TransportModesDialog(
            onResult = { /*TODO*/ },
            preferredModes = TransportMode.selectableModes(),
            onDismiss = { /*TODO*/ },
        )
    }
}
