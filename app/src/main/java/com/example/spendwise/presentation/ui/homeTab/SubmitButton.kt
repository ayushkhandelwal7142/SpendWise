package com.example.spendwise.presentation.ui.homeTab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spendwise.presentation.viewModel.ExpenseViewModel
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SubmitButton(viewModel: ExpenseViewModel) {
    val state = LocalExpenseEntryState.current
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        onClick = {
            val amount = state.amountText.value.toDoubleOrNull()
            if (state.title.value.isEmpty()) {
                state.isTitleError.value = true
            } else if (state.amountText.value.isEmpty()) {
                state.isAmountError.value = true
            } else if (state.selectedCategory.value == null || state.selectedCategory.value!!.categoryName == "Select Category") {
                state.isSelectedCategoryError.value = true
            } else {
                viewModel.onExpenseEntrySubmitClick(
                    title = state.title.value,
                    amount = amount!!,
                    categoryName = state.selectedCategory.value!!,
                    notes = state.notes.value,
                    imageUrl = state.receiptImageUrl.value,
                    date = state.selectedDateInMillis.value ?: fromLocalDate(LocalDate.now())
                )

                state.title.value = ""
                state.amountText.value = ""
                state.selectedCategory.value = null
                state.notes.value = ""
                state.receiptImageUrl.value = ""
            }
        }
    ) {
        Text("Submit")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun fromLocalDate(localDate: LocalDate): Long {
    return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}