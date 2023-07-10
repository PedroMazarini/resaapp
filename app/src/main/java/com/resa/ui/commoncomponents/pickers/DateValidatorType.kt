package com.resa.ui.commoncomponents.pickers

import android.widget.DatePicker
import java.util.Date

enum class DateValidatorType {
    None,
    NowOrLater,
}

fun DatePicker.validate(dateValidatorType: DateValidatorType) {
    when (dateValidatorType) {
        DateValidatorType.None -> {}
        DateValidatorType.NowOrLater -> {
            this.minDate = Date().time
        }
    }
}


