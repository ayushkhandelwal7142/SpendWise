package com.example.spendwise.data.firestoreDb.api

import com.example.spendwise.data.roomDb.model.ExpenseEntity

interface FirestoreApi {
    suspend fun addExpenseToFirestore(expense: ExpenseEntity)
    suspend fun syncFromFirestore()
    suspend fun addDummyExpenses(count: Int)

}