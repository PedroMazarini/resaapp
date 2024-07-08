package com.mazarini.resa.ui.commoncomponents.dialogs

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun RadioOption(
    name: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MTheme.colors.primary,
                unselectedColor = MTheme.colors.textSecondary,
            )
        )
        Text(
            text = name,
            style = MTheme.type.secondaryText,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
@Previews
fun RadioOptionPreview() {
    ResaTheme {
        RadioOption(
            name = "Option 1",
            selected = false,
            onClick = { /*TODO*/ }
        )
    }
}