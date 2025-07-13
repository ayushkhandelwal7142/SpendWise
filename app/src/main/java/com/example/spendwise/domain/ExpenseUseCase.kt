package com.example.spendwise.domain

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private const val DATE_FORMAT = "dd-MM-yyyy"

@RequiresApi(Build.VERSION_CODES.O)
class ExpenseUseCase {

    fun getStartAndEndOfDayFromMillis(epochMillis: Long): Pair<Long, Long> {
        val zoneId = ZoneId.systemDefault()

        val localDate = Instant.ofEpochMilli(epochMillis)
            .atZone(zoneId)
            .toLocalDate()

        val startOfDay = localDate.atStartOfDay(zoneId).toInstant().toEpochMilli()
        val endOfDay = localDate.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli() - 1

        return Pair(startOfDay, endOfDay)
    }

    fun convertDateInMillisToLocalDate(dateInMillis: Long): String {
        val localDate: LocalDate = Instant.ofEpochMilli(dateInMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
        return localDate.format(formatter)
    }

}