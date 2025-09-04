package com.example.spendwise.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.spendwise.data.firestoreDb.usecase.FirestoreUseCase
import com.example.spendwise.data.roomDb.dao.ExpenseDao
import com.example.spendwise.data.roomDb.model.ExpenseEntity

class ExpenseDataRepository(
    private val expenseDao: ExpenseDao,
    private val firestoreApi: FirestoreUseCase,
) {
    suspend fun addExpenseData(data: ExpenseEntity) {
        expenseDao.addExpenseData(data)
        firestoreApi.addExpenseToFirestore(data)
    }

    suspend fun insertAllExpenses(expenses: List<ExpenseEntity>) {
        expenseDao.insertAll(expenses)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun syncFromFirestore() =
        firestoreApi.syncFromFirestore()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addDummyExpenses(count: Int) =
        firestoreApi.addDummyExpenses(count = count)

    fun getExpenseListForParticularCategory(category: String) =
        expenseDao.getExpenseListForParticularCategory(category)

    fun getTotalAmountCategoryWise() = expenseDao.getTotalAmountCategoryWise()

    fun getExpensesForDate(startDate: Long, endDate: Long) =
        expenseDao.getExpensesBetweenDates(
            startOfDay = startDate,
            endOfDay = endDate,
        )
}