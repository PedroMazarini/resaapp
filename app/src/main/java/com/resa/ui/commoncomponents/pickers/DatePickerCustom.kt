package com.resa.ui.commoncomponents.pickers

import android.text.format.DateUtils
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.resa.R
import com.resa.global.extensions.stringRes
import com.resa.ui.theme.MTheme
import java.util.Date

/**
 *
 *
 * TODO - Implement this new DatePicker when in a better shape, currently colors does not work
 *
 *
 */
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DatePickerCustom(
//    confirmText: String = R.string.ok.stringRes()
//    dismissText: String = R.string.cancel.stringRes(),
//    datePickerState: DatePickerState = rememberDatePickerState(),
//    dateValidatorType: DateValidatorType = DateValidatorType.None,
//    onConfirm: () -> Unit = {},
//    onDismiss: () -> Unit = {},
//    onDismissRequest: () -> Unit = {},
//) {
//
//    DatePickerDialog(
//        onDismissRequest = onDismissRequest,
//        colors = datePickerCustomColors(),
//        confirmButton = {
//            TextButton(
//                onClick = onConfirm,
//            ) {
//                Text(
//                    text = confirmText,
//                    style = MTheme.type.textField,
//                )
//            }
//        },
//        dismissButton = {
//            TextButton(
//                onClick = onDismiss,
//            ) {
//                Text(
//                    text = dismissText,
//                    style = MTheme.type.textField,
//                )
//            }
//        }
//    ) {
//        DatePicker(
//            state = datePickerState,
////            dateValidator = { dateValidatorType.validate(it) },
//            colors = datePickerCustomColors(),
//        )
//    }
//}
//
//@Composable
//@OptIn(ExperimentalMaterial3Api::class)
//fun datePickerCustomColors() =
//    DatePickerDefaults.colors(
//        containerColor = MTheme.colors.background,
//        titleContentColor = MTheme.colors.textPrimary,
//        headlineContentColor = MTheme.colors.textPrimary,
//        weekdayContentColor = MTheme.colors.textPrimary,
//        subheadContentColor = MTheme.colors.textPrimary,
//        yearContentColor = MTheme.colors.textPrimary,
//        currentYearContentColor = MTheme.colors.textPrimary,
//        selectedYearContentColor = MTheme.colors.textPrimary,
//        selectedYearContainerColor = MTheme.colors.background,
//        dayContentColor = MTheme.colors.textSecondary,
//        disabledDayContentColor = MTheme.colors.textTertiary,
//        selectedDayContentColor = MTheme.colors.background,
//        disabledSelectedDayContentColor = MTheme.colors.textTertiary,
//        selectedDayContainerColor = MTheme.colors.primary,
//        disabledSelectedDayContainerColor = MTheme.colors.background,
//        todayContentColor = MTheme.colors.textSecondary,
//        todayDateBorderColor = MTheme.colors.primary,
//        dayInSelectionRangeContentColor = MTheme.colors.background,
//        dayInSelectionRangeContainerColor = MTheme.colors.primary,
//    )
//
//
//enum class DateValidatorType {
//    None,
//    NowOrLater,
//}
//
//fun DateValidatorType.validate(dateMillis: Long): Boolean {
//    return when (this) {
//        DateValidatorType.None -> true
//        DateValidatorType.NowOrLater -> DateUtils.isToday(dateMillis) || dateMillis > Date().time
//    }
//}