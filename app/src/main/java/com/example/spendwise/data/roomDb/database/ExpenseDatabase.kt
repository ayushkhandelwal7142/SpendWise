package com.example.spendwise.data.roomDb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.spendwise.data.EXPENSE_DB
import com.example.spendwise.data.roomDb.model.ExpenseEntity
import com.example.spendwise.data.roomDb.dao.ExpenseDao

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract val getExpenseDao: ExpenseDao

    companion object {

        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun buildDatabase(context: Context) = synchronized(this) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context = context,
                    klass = ExpenseDatabase::class.java,
                    name = EXPENSE_DB,
                ).build()
            }

           return@synchronized INSTANCE
        }
    }
}