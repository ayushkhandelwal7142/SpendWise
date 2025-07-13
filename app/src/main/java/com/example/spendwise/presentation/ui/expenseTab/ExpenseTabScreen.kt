package com.example.spendwise.presentation.ui.expenseTab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.spendwise.data.roomDb.model.ExpenseCategoryEnum
import com.example.spendwise.presentation.model.ExpenseTabScreenState
import com.example.spendwise.presentation.viewModel.ExpenseViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

val LocalExpenseTabScreenState = compositionLocalOf<ExpenseTabScreenState> {
    error("ExpenseEntryScreenStates not provided")
}

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseTabScreen(viewModel: ExpenseViewModel) {
    val uiInteractionState by viewModel.expenseUiInteractionState.collectAsState()
    val viewExpenseDate = remember { mutableStateOf("") }
    val isChecked = remember { mutableStateOf(true) }
    val expenseList = uiInteractionState.expenseList
    val isExpanded = remember { mutableStateOf(false) }
    val selectedCategory = remember { mutableStateOf<ExpenseCategoryEnum?>(null) }
    val selectedDate = remember { mutableStateOf(viewModel.getTodayDate()) }
    val selectedDateInMillis = remember { mutableStateOf<Long?>(null) }

    val today: LocalDate = LocalDate.now()
    val todayInMillis = today
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    val state = remember {
        ExpenseTabScreenState(
            viewExpenseDate = viewExpenseDate,
            isChecked = isChecked,
            isExpanded = isExpanded,
            selectedCategory = selectedCategory,
            selectedDate = selectedDate,
            selectedDateInMillis = selectedDateInMillis,
        )
    }

    CompositionLocalProvider(value = LocalExpenseTabScreenState provides state) {

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
                        .background(color = Color.LightGray)
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        CategoryDropDownView(
                            modifier = Modifier.weight(0.75f),
                            isChecked = isChecked.value,
                        ) { category ->
                            viewModel.getExpenseForParticularCategory(category = category)
                        }

                        Switch(
                            modifier = Modifier.weight(0.75f),
                            checked = isChecked.value,
                            onCheckedChange = { isChecked.value = it }
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(color = if (isChecked.value) Color.Green else Color.Transparent)
                        ) {
                            DateView(
                                dateText = selectedDate.value,
                                isChecked = isChecked.value
                            ) { dateInMillis ->
                                dateInMillis?.let {
                                    selectedDate.value =
                                        viewModel.convertDateInMillisToLocalDate(dateInMillis = it)
                                    viewModel.getExpenseListForParticularDate(dateInMillis = it)
                                }
                            }
                        }
                    }
                }

            }

            item { Spacer(modifier = Modifier.padding(8.dp)) }

            expenseList?.let {
                itemsIndexed(it) { index, item ->
                    ExpenseItem(
                        title = item.title,
                        amount = item.amount,
                        category = item.category,
                        notes = item.notes,
                        date = item.toLocalDate(item.date)!!
                            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        index = index,
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getExpenseListForParticularDate(dateInMillis = todayInMillis)
    }

    LaunchedEffect(key1 = isChecked.value) {
        if (isChecked.value) {
            selectedDateInMillis.value?.let { viewModel.getExpenseListForParticularDate(dateInMillis = it) }
        } else {
            selectedCategory.value?.let { viewModel.getExpenseForParticularCategory(category = it.categoryName) }
        }
    }
}