package com.example.spendwise.presentation.viewModel.uiState

import com.example.spendwise.data.roomDb.model.CategoryWiseSpendModel
import com.example.spendwise.data.roomDb.model.ExpenseCategoryEnum
import com.example.spendwise.data.roomDb.model.ExpenseEntity
import com.example.spendwise.presentation.viewModel.uiState.UiState.IDLE
import org.w3c.dom.Text
import java.time.LocalDate

data class ExpenseUiInteractionState(
    val expenseEntryScreenUiState: UiState = IDLE,
    val newExpenseText: Text? = null,
    val newExpenseAmount: Long? = null,
    val newExpenseCategoryEnum: ExpenseCategoryEnum? = null,
    val newExpenseNotes: String? = null,
    val newExpenseImageUrl: String? = null,
    val newExpenseDate: LocalDate? = null,
    val todayTotalSpent: Double = 0.0,
    val selectedDateText: String = "",
    val selectedDateInMillis: Long? = null,
    val expenseList: List<ExpenseEntity>? = null,
    val categoryWiseSpend: List<CategoryWiseSpendModel>? = null,
)
