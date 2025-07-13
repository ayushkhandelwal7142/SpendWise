package com.example.spendwise.presentation.ui.homeTab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.spendwise.presentation.ui.DatePicker

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateSelector(
    modifier: Modifier = Modifier,
    dateText: String,
    onDateSelected: (Long?) -> Unit,
) {
    val state = LocalExpenseEntryState.current
    DatePicker(
        modifier = modifier,
        date = dateText,
        isChecked = true,
    ) { millis ->
        state.selectedDateInMillis.value = millis
        onDateSelected(millis)
    }
}