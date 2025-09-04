package com.example.spendwise.data.firestoreDb.usecase

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.spendwise.data.firestoreDb.api.FirestoreApi
import com.example.spendwise.data.firestoreDb.model.FirestoreExpenseEntity
import com.example.spendwise.data.roomDb.dao.ExpenseDao
import com.example.spendwise.data.roomDb.model.ExpenseEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreUseCase(
    private val expenseDao: ExpenseDao,
    private val firestore: FirebaseFirestore,
): FirestoreApi {
    private val collectionRef = firestore.collection("expenses")

    override suspend fun addExpenseToFirestore(expense: ExpenseEntity) {
        Log.e("AYUSH::", "AYUSH:: adding dummy data to firestore")
        collectionRef.document(expense.title).set(expense).await()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun syncFromFirestore() {
        val firestoreData = fetchFromFirestore()
        firestoreData.forEach { expense ->
            expenseDao.addExpenseData(expense.toExpenseEntity())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addDummyExpenses(count: Int) {
        val dummyList = (1..count).map {
            ExpenseEntity(
                title = "Dummy Expense ${(1..500).random()}",
                amount = (10..500).random().toDouble(),
                category = if (it % 2 == 0) "Food" else if (it % 3 == 0) "Utility" else if (it % 5 == 0) "Staff" else "Travel",
                date = System.currentTimeMillis(),
                notes = "Dummy Data $it",
                receiptImageUrl = ""
            )
        }
        dummyList.forEach { expense ->
            addExpenseToFirestore(expense)
        }
    }

    private suspend fun fetchFromFirestore(): List<FirestoreExpenseEntity> {
        return try {
            val snapshot = collectionRef.get().await()
            snapshot.documents.mapNotNull { it.toObject(FirestoreExpenseEntity::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}