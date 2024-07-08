package com.mazarini.resa.ui.commoncomponents.pickers

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