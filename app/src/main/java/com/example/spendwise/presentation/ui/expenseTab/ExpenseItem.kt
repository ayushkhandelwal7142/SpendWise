package com.example.spendwise.presentation.ui.expenseTab

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ExpenseItem(
    modifier: Modifier = Modifier,
    title: String,
    amount: Double,
    category: String,
    notes: String = "",
    date: String,
    index: Int,
) {
    Box (
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(2.dp)
            .background(color = if (index % 2 == 0) Color(0xFFD0E8FF) else Color.Transparent)
            .border(width = 1.dp, shape = ShapeDefaults.Medium, color = Color.Black)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row {
                Text(text = title)

                Spacer(modifier = Modifier.weight(1f))

                Text(text = "₹$amount")
            }

            Row {
                Text(text = "Category: $category")

                Spacer(modifier = Modifier.weight(1f))

                Text(text = "Date: $date")
            }

            Text(text = "Notes: $notes")
        }
    }
}

@Preview
@Composable
fun ExpenseItemPreview() {
    ExpenseItem(
        title = "Startbucks",
        amount = 230.0,
        category = "Food",
        notes = "Burger, Pizza, Pasta",
        date = "12-07-2025",
        index = 1,
    )
}