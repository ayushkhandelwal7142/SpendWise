package com.example.spendwise.data.roomDb.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.spendwise.data.EXPENSE_DATA
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Entity(tableName = EXPENSE_DATA)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val notes: String,
    val receiptImageUrl: String,
    val date: Long,
) {

    fun fromLocalDate(localDate: LocalDate?): Long? {
        return localDate?.atStartOfDay(ZoneId.systemDefault())
            ?.toInstant()
            ?.toEpochMilli()
    }

    fun toLocalDate(millis: Long?): LocalDate? {
        return millis?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
    }
}
