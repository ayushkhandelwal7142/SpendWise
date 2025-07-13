package com.example.spendwise.presentation.ui

import android.net.Uri
import android.os.Build
import android.widget.Space
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.spendwise.R.*
import com.example.spendwise.data.roomDb.model.ExpenseCategoryEnum
import com.example.spendwise.presentation.viewModel.ExpenseViewModel
import com.example.spendwise.presentation.viewModel.uiState.UiState.*
import com.example.spendwise.ui.theme.SpendWiseTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val MAX_CHARS = 100

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseEntryScreen(viewModel: ExpenseViewModel) {

    val uiInteractionState by viewModel.expenseUiInteractionState.collectAsState()
    val uiState = uiInteractionState.expenseEntryScreenUiState
    val todayTotalSpent = uiInteractionState.todayTotalSpent
    var selectedDate = uiInteractionState.selectedDateText
    val receiptImageUrl = uiInteractionState.newExpenseImageUrl

    val context = LocalContext.current


    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf<Double?>(null) }
    var selectedCategory by remember { mutableStateOf<ExpenseCategoryEnum?>(null) }
    var notes by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    var isTitleError by remember { mutableStateOf(false) }
    var isAmountError by remember { mutableStateOf(false) }
    var isCategoryNameError by remember { mutableStateOf(false) }
    var isReceiptImageUrlError by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) }
    val calendar = Calendar.getInstance()
    var selectedDateInMillis by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    //var imagePathUri by remember { mutableStateOf("") }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.saveReceiptImage(
                context = context,
                imageUri = it,
            )
        }
    }

    var painter = rememberAsyncImagePainter(model = File(receiptImageUrl ?: ""))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        when (uiState) {
            is LOADING -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }

            is SUCCESS -> {
                Toast.makeText(context, "Expense Entry added successfully", Toast.LENGTH_LONG).show()
                viewModel.updateExpenseEntryScreenUiState(state = IDLE)
            }

            is ERROR -> {
                Toast.makeText(context, uiState.errorMsg, Toast.LENGTH_LONG).show()
                viewModel.updateExpenseEntryScreenUiState(state = IDLE)
            }

            IDLE -> {}
        }

        TodayTotalSpend(
            modifier = Modifier.padding(top = 40.dp),
            todayTotalSpent = todayTotalSpent,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Add a new Expense",
            fontWeight = FontWeight.Bold,
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            value = title,
            onValueChange = {
                title = it
                isTitleError = title.isEmpty()
            },
            label = { Text(text = "Title") },
            placeholder = { Text(text = "Enter Expense Title") },
            isError = isTitleError,
            supportingText = { if (isTitleError) Text(text = "This field cannot be blank") }
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            value = amountText,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                    amountText = newValue
                    amount = newValue.toDoubleOrNull()
                    isAmountError = amount == null
                }
            },
            label = { Text(text = "Amount") },
            placeholder = { Text(text = "Enter Expense Title") },
            isError = isAmountError,
            supportingText = {
                if (isAmountError) Text(text = "Enter correct amount")
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,

            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp)
                        .fillMaxWidth()
                        .height(40.dp)
                        .clickable { isExpanded = isExpanded.not() }
                        .border(
                            width = 2.dp,
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Black,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier.padding(
                            start = 8.dp
                        ),
                        text = selectedCategory?.categoryName ?: "Select Category",
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        modifier = Modifier.padding(end = 8.dp),
                        painter = painterResource(drawable.ic_arrow_drop_down),
                        contentDescription = "Drop Down Arrow"
                    )
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp)
                ) {
                    DatePicker(
                        modifier = Modifier
                            .weight(0.5f),
                        date = selectedDate,
                        isChecked = true,
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
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(horizontal = 32.dp, vertical = 8.dp),
            value = notes,
            onValueChange = { newValue ->
                if (newValue.length <= MAX_CHARS)
                    notes = newValue
            },
            label = { Text(text = "Notes(Optional)") },
            placeholder = { Text(text = "Max 100 chars") },
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 12.dp),
            onClick = {
                imagePicker.launch("image/*")
            }
        ) {
            Text(text = "Upload Receipt")
        }

        Image(
            painter = painter,
            contentDescription = "Receipt Image",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 12.dp),
            onClick = {
                if (title.isEmpty()) {
                    isTitleError = true
                } else if (amount == null) {
                    isAmountError = true
                } else if (selectedCategory == null) {
                    isCategoryNameError = true
                } else {
                    viewModel.onExpenseEntrySubmitClick(
                        title = title,
                        amount = amount!!,
                        categoryName = selectedCategory!!,
                        notes = notes,
                        imageUrl = receiptImageUrl ?: "",
                        date = selectedDateInMillis!!,
                    )

                    title = ""
                    amountText = ""
                    selectedCategory = null
                    notes = ""
                    viewModel.clearImageUri()
                }
            }
        ) {
            Text(text = "Submit")
        }
    }
}

@Preview
@Composable
fun ExpenseEntryScreenPreview() {
    SpendWiseTheme {
        //ExpenseEntryScreen()
    }
}