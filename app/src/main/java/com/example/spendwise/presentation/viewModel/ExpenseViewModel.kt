package com.example.spendwise.presentation.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendwise.data.roomDb.model.CategoryWiseSpendModel
import com.example.spendwise.data.roomDb.model.ExpenseCategoryEnum
import com.example.spendwise.data.roomDb.model.ExpenseEntity
import com.example.spendwise.data.roomDb.repository.ExpenseDataRepository
import com.example.spendwise.domain.ExpenseUseCase
import com.example.spendwise.presentation.viewModel.uiState.ExpenseUiInteractionState
import com.example.spendwise.presentation.viewModel.uiState.UiState
import com.example.spendwise.presentation.viewModel.uiState.UiState.ERROR
import com.example.spendwise.presentation.viewModel.uiState.UiState.SUCCESS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class ExpenseViewModel(
    private val expenseUseCase: ExpenseUseCase,
    private val expenseDataRepository: ExpenseDataRepository,
) : ViewModel() {

    private val _expenseUiInteractionState = MutableStateFlow(ExpenseUiInteractionState())
    val expenseUiInteractionState = _expenseUiInteractionState.asStateFlow()

    fun updateExpenseEntryScreenUiState(state: UiState) {
        _expenseUiInteractionState.update { it.copy(expenseEntryScreenUiState = state) }
    }

    fun updateExpenseList(list: List<ExpenseEntity>) {
        _expenseUiInteractionState.update { it.copy(expenseList = list) }
    }

    fun updateCategoryWiseExpense(data: List<CategoryWiseSpendModel>) {
        _expenseUiInteractionState.update { it.copy(categoryWiseSpend = data) }
    }

    fun updateReceiptImageUri(uri: String) {
        _expenseUiInteractionState.update { it.copy(newExpenseImageUrl = uri) }
    }

    fun convertDateInMillisToLocalDate(dateInMillis: Long) =
        expenseUseCase.convertDateInMillisToLocalDate(dateInMillis = dateInMillis)

    fun getTodayDate(): String {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    }

    fun insertMockExpenses(expenses: List<ExpenseEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseDataRepository.insertAllExpenses(expenses)
        }
    }

    fun getExpenseListForParticularDate(dateInMillis: Long) {
        val (start, end) = expenseUseCase.getStartAndEndOfDayFromMillis(dateInMillis)
        viewModelScope.launch {
            try {
                expenseDataRepository.getExpensesForDate(start, end)
                    .collectLatest { expenseList ->
                        updateExpenseList(list = expenseList)
                    }
            } catch (e: Exception) {
                e.localizedMessage ?: "Error"
            }
        }
    }

    fun getExpenseForParticularCategory(category: String) {
        viewModelScope.launch {
            try {
                expenseDataRepository.getExpenseListForParticularCategory(category = category)
                    .collectLatest { expenseList ->
                        Log.d("AYUSH::", "AYUSH:: expenseList = $expenseList")
                        updateExpenseList(list = expenseList)
                    }
            } catch (e: Exception) {
                e.localizedMessage ?: "Error"
            }
        }
    }

    fun getTotalAmountCategoryWise() {
        viewModelScope.launch {
            try {
                expenseDataRepository.getTotalAmountCategoryWise()
                    .collectLatest { entry ->
                        updateCategoryWiseExpense(data = entry)
                    }
            } catch (e: Exception) {
                e.localizedMessage ?: "Error"
            }
        }
    }

    fun onExpenseEntrySubmitClick(
        title: String,
        amount: Double,
        categoryName: ExpenseCategoryEnum,
        notes: String,
        imageUrl: String,
        date: Long,
    ) {
        val expenseEntryData = ExpenseEntity(
            title = title,
            amount = amount,
            category = categoryName.categoryName,
            notes = notes,
            receiptImageUrl = imageUrl,
            date = date
        )

        viewModelScope.launch {
            updateExpenseEntryScreenUiState(state = UiState.LOADING)

            try {
                expenseDataRepository.addExpenseData(data = expenseEntryData)
                updateExpenseEntryScreenUiState(state = SUCCESS)
            } catch (e: Exception) {
                Log.d("AYUSH::", "AYUSH:: ${e.localizedMessage ?: "Error"}")
                updateExpenseEntryScreenUiState(
                    state = ERROR(
                        e.localizedMessage ?: "Error Adding Data to RoomDb"
                    )
                )
            }
        }
    }

    fun updateTodayTotalSpend() {
        val today = LocalDate.now()
        val todayInMillis = today
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val (start, end) = expenseUseCase.getStartAndEndOfDayFromMillis(todayInMillis)

        var totalAmount = 0.0
        try {
            viewModelScope.launch {
                expenseDataRepository.getExpensesForDate(start, end)
                    .collect { expenseList ->
                        totalAmount = expenseList.sumOf { it.amount }
                        _expenseUiInteractionState.update {
                            it.copy(todayTotalSpent = totalAmount)
                        }
                    }
            }
        } catch (e: Exception) {
            Log.d("AYUSH::", "AYUSH:: ${e.localizedMessage ?: "Error"}")
        }
    }

    fun updateSelectedDate(dateInMillis: Long) {
        val date = expenseUseCase.convertDateInMillisToLocalDate(dateInMillis = dateInMillis)
        _expenseUiInteractionState.update { it.copy(selectedDateText = date) }
    }
}