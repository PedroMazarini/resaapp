package com.resa.ui.commoncomponents.pickers

import android.app.DatePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.resa.global.extensions.day
import com.resa.global.extensions.month
import com.resa.global.extensions.year
import java.util.Calendar
import java.util.Date

@Composable
fun datePicker(
    initialDate: Date,
    validatorType: DateValidatorType = DateValidatorType.None,
    onDateSelected: (date: Date) -> Unit,
): DatePickerDialog {
    val context = LocalContext.current
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year: Int, month: Int, day: Int ->
            onDateSelected(getDate(year, month, day))
        },
        initialDate.year(),
        initialDate.month(),
        initialDate.day(),
    )
    datePickerDialog.datePicker.validate(validatorType)
    return datePickerDialog
}

private fun getDate(year: Int, month: Int, day: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, day)
    return calendar.time
}
