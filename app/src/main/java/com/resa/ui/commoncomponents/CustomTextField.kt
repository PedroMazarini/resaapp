package com.resa.ui.commoncomponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.resa.ui.theme.MTheme

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    maxLines: Int = 1,
    placeHolder: String = "",
    textStyle: TextStyle = MTheme.type.textField,
    placeHolderStyle: TextStyle = MTheme.type.textFieldPlaceHolder,
) =
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onValueChange(it) },
        textStyle = textStyle.copy(),
        maxLines = maxLines,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        decorationBox = { innerTextField ->
            Row(modifier = Modifier.fillMaxWidth()) {
                if (value.isEmpty()) {
                    Text(
                        modifier = Modifier.align(CenterVertically),
                        text = placeHolder,
                        style = placeHolderStyle,
                    )
                }
            }
            innerTextField()
        }
    )
