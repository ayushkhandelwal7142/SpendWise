package com.example.spendwise.presentation.model

import androidx.compose.runtime.MutableState
import com.example.spendwise.data.roomDb.model.ExpenseCategoryEnum

data class ExpenseTabScreenState(
    val viewExpenseDate: MutableState<String>,
    val isChecked: MutableState<Boolean>,
    val isExpanded: MutableState<Boolean>,
    val selectedCategory: MutableState<ExpenseCategoryEnum?>,
    val selectedDate: MutableState<String>,
    val selectedDateInMillis: MutableState<Long?>,
)
