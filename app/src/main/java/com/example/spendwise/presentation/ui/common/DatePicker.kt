package com.example.spendwise.presentation.ui.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spendwise.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private const val CTA_TEXT_OK = "OK"
private const val CTA_TEXT_CANCEL = "Cancel"
private const val CONTENT_DESCRIPTION = "Date Picker"

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(
    isChecked: Boolean,
    date: String,
    modifier: Modifier = Modifier,
    selectedDateMillis: (Long?) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) }

    Row (
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(8.dp),
                color = Color.Black
            )
            .padding(10.dp)
            .clickable { if (isChecked) showDatePicker = showDatePicker.not() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(10.dp)),
            painter = painterResource(id = R.drawable.ic_calendar),
            contentDescription = CONTENT_DESCRIPTION,
        )

        Text(text = text)
    }

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { date ->
                date?.let {
                    text = convertDateInMillisToLocalDate(dateInMillis = it)
                    showDatePicker = false
                    selectedDateMillis(it)
                }
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                }
            ) {
                Text(CTA_TEXT_OK)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(CTA_TEXT_CANCEL)
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertDateInMillisToLocalDate(dateInMillis: Long): String {
    val localDate: LocalDate = Instant.ofEpochMilli(dateInMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return localDate.format(formatter)
}

@Preview
@Composable
private fun CalendarPreview() {
    //DatePicker() {}
}