package com.example.spendwise.data.roomDb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.spendwise.data.roomDb.model.ExpenseEntity
import com.example.spendwise.data.roomDb.model.CategoryWiseSpendModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(expenses: List<ExpenseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExpenseData(data: ExpenseEntity)

    @Query("SELECT * FROM expenseData")
    fun getExpenseData(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenseData WHERE date BETWEEN :startOfDay AND :endOfDay")
    fun getExpensesBetweenDates(startOfDay: Long, endOfDay: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT amount FROM expenseData WHERE date BETWEEN :startOfDay AND :endOfDay")
    fun getAmountListForExpensesBetweenDates(startOfDay: Long, endOfDay: Long): Flow<List<Double>>

    @Query("SELECT category, SUM(amount) as totalAmount FROM expenseData GROUP BY category ORDER BY totalAmount DESC")
    fun getTotalAmountCategoryWise(): Flow<List<CategoryWiseSpendModel>>

    @Query("SELECT * FROM expenseData WHERE category = :category")
    fun getExpenseListForParticularCategory(category: String): Flow<List<ExpenseEntity>>
}