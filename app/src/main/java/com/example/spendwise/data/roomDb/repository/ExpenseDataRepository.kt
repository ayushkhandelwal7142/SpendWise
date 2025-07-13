package com.example.spendwise.data.roomDb.repository

import com.example.spendwise.data.roomDb.dao.ExpenseDao
import com.example.spendwise.data.roomDb.model.ExpenseEntity

class ExpenseDataRepository(
    private val expenseDao: ExpenseDao,
) {
    suspend fun addExpenseData(data: ExpenseEntity) = expenseDao.addExpenseData(data = data)

    fun getExpenseData() = expenseDao.getExpenseData()

    fun getExpensesForDate(startDate: Long, endDate: Long) =
        expenseDao.getExpensesBetweenDates(
            startOfDay = startDate,
            endOfDay = endDate,
        )

    fun getExpenseListForParticularCategory(category: String) =
        expenseDao.getExpenseListForParticularCategory(category = category)

    fun getTotalAmountCategoryWise() = expenseDao.getTotalAmountCategoryWise()
}