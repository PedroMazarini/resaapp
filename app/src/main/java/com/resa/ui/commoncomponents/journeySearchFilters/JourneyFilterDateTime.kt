package com.resa.ui.commoncomponents.journeySearchFilters

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resa.R
import com.resa.global.extensions.asCardBackground
import com.resa.global.extensions.asCardElevation
import com.resa.global.extensions.date_MMM_dd_spaced
import com.resa.global.extensions.stringRes
import com.resa.global.extensions.time_HH_mm_spaced
import com.resa.ui.commoncomponents.pickers.DateValidatorType
import com.resa.ui.commoncomponents.pickers.datePicker
import com.resa.ui.commoncomponents.pickers.timePicker
import com.resa.ui.screens.locationsearch.model.JourneyFilters
import com.resa.ui.screens.locationsearch.state.LocationSearchUiState
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import java.util.Date

@Composable
fun JourneyFilterDateTime(
    modifier: Modifier,
    filters: JourneyFilters,
    onDateChanged: (Date) -> Unit,
    onTimeChanged: (Date) -> Unit,
) {

    val datePicker = initiateDatePicker(filters, onDateChanged)

    val timePicker = initiateTimePicker(filters, onTimeChanged)

    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 24.dp)
                    .align(Alignment.CenterVertically),
                text = R.string.date.stringRes(),
                style = MTheme.type.textField.copy(color = MTheme.colors.lightText),
            )
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = 0.dp.asCardElevation(),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 24.dp)
                    .clickable { datePicker.show() },
                colors = MTheme.colors.btnBackground.asCardBackground(),
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    text = filters.date.date_MMM_dd_spaced(),
                    style = MTheme.type.textFieldPlaceHolder,
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally),
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 24.dp)
                    .align(Alignment.CenterVertically),
                text = R.string.time.stringRes(),
                style = MTheme.type.textField.copy(color = MTheme.colors.lightText),
            )
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = 0.dp.asCardElevation(),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 24.dp)
                    .clickable { timePicker.show() },
                colors = MTheme.colors.btnBackground.asCardBackground(),
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    text = filters.date.time_HH_mm_spaced(),
                    style = MTheme.type.textFieldPlaceHolder,
                )
            }
        }
    }
}

@Composable
private fun initiateDatePicker(
    filters: JourneyFilters,
    onDateChanged: (Date) -> Unit,
): DatePickerDialog =
    datePicker(
        initialDate = filters.date,
        validatorType = DateValidatorType.NowOrLater
    ) {
        onDateChanged(it)
    }

@Composable
private fun initiateTimePicker(
    filters: JourneyFilters,
    onTimeChanged: (Date) -> Unit,
): TimePickerDialog =
    timePicker(
        time = filters.date,
    ) {
        onTimeChanged(it)
    }

@Composable
@Preview
fun JourneyFilterDateTimePreview() {
    ResaTheme {
        Column(modifier = Modifier.background(Color.White)) {
            JourneyFilterDateTime(
                modifier = Modifier,
                filters = JourneyFilters(),
                onDateChanged = {},
                onTimeChanged = {},
            )
        }
    }
}
