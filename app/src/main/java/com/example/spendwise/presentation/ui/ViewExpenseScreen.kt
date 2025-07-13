package com.example.spendwise.presentation.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.spendwise.R
import com.example.spendwise.data.roomDb.model.ExpenseCategoryEnum
import com.example.spendwise.presentation.viewModel.ExpenseViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewExpenseScreen(viewModel: ExpenseViewModel) {
    val uiInteractionState by viewModel.expenseUiInteractionState.collectAsState()
    var viewExpenseDate by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(true) }
    val expenseList = uiInteractionState.expenseList
    var isExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<ExpenseCategoryEnum?>(null) }
    var selectedDate by remember { mutableStateOf(viewModel.getTodayDate()) }
    var selectedDateInMillis by remember { mutableStateOf<Long?>(null) }

    val today: LocalDate = LocalDate.now()
    val todayInMillis = today
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        stickyHeader {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Row {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .height(40.dp)
                            .clickable { if (isChecked.not()) isExpanded = isExpanded.not() }
                            .border(
                                width = 2.dp,
                                shape = RoundedCornerShape(8.dp),
                                color = Color.Black,
                            )
                            .background(color = if (isChecked) Color.Gray else Color.White),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = selectedCategory?.categoryName ?: "Category")

                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_drop_down),
                            contentDescription = "Drop Down Arrow"
                        )
                    }

                    Switch(
                        modifier = Modifier.weight(1f),
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }
                    )

                    Row(
                        modifier = Modifier.weight(1f)
                    ) {
                        DatePicker(
                            modifier = Modifier
                                .weight(0.5f)
                                .background(color = if (isChecked.not()) Color.Gray else Color.White),
                            date = selectedDate,
                            isChecked = isChecked,
                        ) { date ->
                            date?.let {
                                selectedDateInMillis = date
                                selectedDate = viewModel.convertDateInMillisToLocalDate(dateInMillis = date)
                                viewModel.getExpenseListForParticularDate(dateInMillis = date)
                            }
                        }
                    }
                }

                DropdownMenu(
                    modifier = Modifier,
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    ExpenseCategoryEnum.entries.forEach {
                        val category = it

                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            text = { Text(text = it.categoryName) },
                            onClick = {
                                selectedCategory = category
                                isExpanded = false
                                selectedCategory?.let { viewModel.getExpenseForParticularCategory(category = selectedCategory!!.categoryName) }
                            }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.padding(8.dp))
        }

        expenseList?.let {
            items(it) { item ->
                ExpenseItem(
                    title = item.title,
                    amount = item.amount,
                    category = item.category,
                    notes = item.notes,
                    date = item.toLocalDate(item.date)!!.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getExpenseListForParticularDate(dateInMillis = todayInMillis)
    }

    LaunchedEffect(isChecked) {
        if (isChecked) {
            selectedDateInMillis?.let {  viewModel.getExpenseListForParticularDate(dateInMillis = selectedDateInMillis!!) }
        } else {
            selectedCategory?.let { viewModel.getExpenseForParticularCategory(category = it.categoryName) }
        }
    }
}