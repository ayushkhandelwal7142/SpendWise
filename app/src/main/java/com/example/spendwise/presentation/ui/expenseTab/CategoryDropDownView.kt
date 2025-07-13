package com.example.spendwise.presentation.ui.expenseTab

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.R
import com.example.spendwise.data.roomDb.model.ExpenseCategoryEnum

@Composable
fun CategoryDropDownView(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCategorySelected: (String) -> Unit
) {
    val state = LocalExpenseTabScreenState.current

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clickable { if (isChecked.not()) expanded = expanded.not() }
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(8.dp),
            )
            .clip(RoundedCornerShape(8.dp))
            .background(color = if (isChecked.not()) Color.Green else Color.Transparent)
            .padding(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = state.selectedCategory.value?.categoryName ?: "Category",
                fontSize = 14.sp
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_drop_down),
                contentDescription = null,
            )
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }) {
        ExpenseCategoryEnum.entries.forEach { category ->
            DropdownMenuItem(
                text = { Text(text = category.categoryName) },
                onClick = {
                    state.selectedCategory.value = category
                    expanded = false
                    onCategorySelected(category.categoryName)
                },
            )
        }
    }
}