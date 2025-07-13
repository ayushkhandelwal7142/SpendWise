package com.example.spendwise.presentation.ui.expenseTab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.spendwise.presentation.ui.common.DatePicker


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateView(
    modifier: Modifier = Modifier,
    dateText: String,
    isChecked: Boolean,
    onDateSelected: (Long?) -> Unit,
) {
    val state = LocalExpenseTabScreenState.current
    DatePicker(
        modifier = modifier,
        date = dateText,
        isChecked = isChecked,
    ) { millis ->
        state.selectedDateInMillis.value = millis
        onDateSelected(millis)
    }
}