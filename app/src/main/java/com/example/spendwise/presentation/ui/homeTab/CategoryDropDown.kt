package com.example.spendwise.presentation.ui.homeTab

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.R
import com.example.spendwise.data.roomDb.model.ExpenseCategoryEnum

@Composable
fun CategoryDropdown(
    modifier: Modifier = Modifier,
) {
    val state = LocalExpenseEntryState.current
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .clickable { expanded = expanded.not() }
            .border(
                width = 1.dp,
                color = if (state.isSelectedCategoryError.value)  Color.Red else Color.Black,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = state.selectedCategory.value?.categoryName ?: "Select Category",
            fontSize = 14.sp
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_drop_down),
            contentDescription = null,
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }) {
        ExpenseCategoryEnum.entries.forEach { category ->
            DropdownMenuItem(
                text = { Text(text = category.categoryName) },
                onClick = {
                    state.isSelectedCategoryError.value = false
                    state.selectedCategory.value = category
                    expanded = false
                },
            )
        }
    }
}