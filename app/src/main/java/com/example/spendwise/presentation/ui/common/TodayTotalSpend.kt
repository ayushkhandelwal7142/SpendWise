package com.example.spendwise.presentation.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TodayTotalSpend(
    modifier: Modifier = Modifier,
    todayTotalSpent: Double,
) {
    Row(modifier = modifier) {
        Text(
            text = "Today's Total Spend = ",
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "$$todayTotalSpent",
            fontWeight = FontWeight.Bold,
        )
    }
}