package com.example.spendwise.presentation.model

import androidx.compose.runtime.MutableState
import com.example.spendwise.data.roomDb.model.ExpenseCategoryEnum

data class HomeTabScreenState(
    val title: MutableState<String>,
    val isTitleError: MutableState<Boolean>,
    val amountText: MutableState<String>,
    val isAmountError: MutableState<Boolean>,
    val selectedCategory: MutableState<ExpenseCategoryEnum?>,
    val isSelectedCategoryError: MutableState<Boolean>,
    val notes: MutableState<String>,
    val selectedDateInMillis: MutableState<Long?>,
    val receiptImageUrl: MutableState<String>,
    val isDateError: MutableState<Boolean>,
)
