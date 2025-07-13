package com.example.spendwise.domain

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
class DateTypeConverters {

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
