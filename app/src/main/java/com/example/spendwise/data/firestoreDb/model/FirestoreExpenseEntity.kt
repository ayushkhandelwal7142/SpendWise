package com.example.spendwise.data.firestoreDb.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.spendwise.data.roomDb.model.ExpenseEntity

data class FirestoreExpenseEntity(
    val id: Int = 0,
    val title: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val notes: String = "",
    val receiptImageUrl: String = "",
    val date: Long = 0L
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun toExpenseEntity() = ExpenseEntity(
        title = title,
        amount = amount,
        category = category,
        notes = notes,
        receiptImageUrl = receiptImageUrl,
        date = date
    )
}