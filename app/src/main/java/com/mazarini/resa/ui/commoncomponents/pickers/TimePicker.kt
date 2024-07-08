package com.mazarini.resa.ui.commoncomponents.pickers

import android.app.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.mazarini.resa.global.extensions.getHour
import com.mazarini.resa.global.extensions.getMinute
import java.util.Calendar
import java.util.Date


@Composable
fun timePicker(
    time: Date,
    onTimeSelected: (Date) -> Unit,
): TimePickerDialog {
    val context = LocalContext.current
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            onTimeSelected(getTime(hour, minute))
        },
        time.getHour(),
        time.getMinute(),
        true,
    )
    return timePickerDialog
}

private fun getTime(hour: Int, minute: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    return calendar.time
}
