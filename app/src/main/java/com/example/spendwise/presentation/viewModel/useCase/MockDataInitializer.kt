package com.example.spendwise.presentation.viewModel.useCase

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.spendwise.data.roomDb.model.ExpenseCategoryEnum
import com.example.spendwise.data.roomDb.model.ExpenseEntity
import com.example.spendwise.presentation.viewModel.ExpenseViewModel
import com.example.spendwise.presentation.viewModel.useCase.AppLaunchUseCase.Companion.setFirstTimeLaunchDone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MockDataInitializer {
    @RequiresApi(Build.VERSION_CODES.O)
    fun populateIfFirstLaunch(context: Context, viewModel: ExpenseViewModel) {
        if (AppLaunchUseCase.isFirstTimeLaunch(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                val mockExpenses = listOf(
                    ExpenseEntity(
                        title = "Tea",
                        amount = 200.0,
                        category = ExpenseCategoryEnum.FOOD.categoryName,
                        notes = "Breakfast",
                        receiptImageUrl = "",
                        date = 1752278400000,
                    ),

                    ExpenseEntity(
                        title = "Train Ticket",
                        amount = 1500.0,
                        category = ExpenseCategoryEnum.TRAVEL.categoryName,
                        notes = "Hyd-Ngp",
                        receiptImageUrl = "",
                        date = 1752451200000,
                    ),

                    ExpenseEntity(
                        title = "Chai Samosa",
                        amount = 500.0,
                        category = ExpenseCategoryEnum.FOOD.categoryName,
                        notes = "",
                        receiptImageUrl = "",
                        date = 1752278400000,
                    ),

                    ExpenseEntity(
                        title = "Security Guard Uniform",
                        amount = 1000.0,
                        category = ExpenseCategoryEnum.STAFF.categoryName,
                        notes = "All Guards Uniform Change",
                        receiptImageUrl = "",
                        date = 1752451200000,
                    ),

                    ExpenseEntity(
                        title = "Flight Ticket",
                        amount = 5000.0,
                        category = ExpenseCategoryEnum.TRAVEL.categoryName,
                        notes = "Trip to Mysore",
                        receiptImageUrl = "",
                        date = 1752278400000,
                    ),

                    ExpenseEntity(
                        title = "Dinner",
                        amount = 1000.0,
                        category = ExpenseCategoryEnum.FOOD.categoryName,
                        notes = "masala Dosa",
                        receiptImageUrl = "",
                        date = 1752278400000,
                    ),

                    ExpenseEntity(
                        title = "Lunch",
                        amount = 600.0,
                        category = ExpenseCategoryEnum.FOOD.categoryName,
                        notes = "Veg Biryani",
                        receiptImageUrl = "",
                        date = 1752278400000,
                    ),

                    ExpenseEntity(
                        title = "Furniture",
                        amount = 20334.0,
                        category = ExpenseCategoryEnum.UTILITY.categoryName,
                        notes = "Office Furniture",
                        receiptImageUrl = "",
                        date = 1752431400000,
                    ),

                    ExpenseEntity(
                        title = "Salary",
                        amount = 12000.0,
                        category = ExpenseCategoryEnum.STAFF.categoryName,
                        notes = "July Month Salary",
                        receiptImageUrl = "",
                        date = 1752278400000,
                    ),

                    ExpenseEntity(
                        title = "Petrol",
                        amount = 2000.0,
                        category = ExpenseCategoryEnum.TRAVEL.categoryName,
                        notes = "Trip to Anantagiri Hills",
                        receiptImageUrl = "",
                        date = 1752451200000,
                    ),
                    )
                viewModel.insertMockExpenses(mockExpenses)
                setFirstTimeLaunchDone(context)
            }
        }
    }
}
