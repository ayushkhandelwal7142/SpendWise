package com.example.spendwise.presentation.ui.homeTab

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.spendwise.R
import com.example.spendwise.data.roomDb.model.ExpenseCategoryEnum
import com.example.spendwise.presentation.model.HomeTabScreenState
import com.example.spendwise.presentation.ui.common.TodayTotalSpend
import com.example.spendwise.presentation.viewModel.ExpenseViewModel
import com.example.spendwise.presentation.viewModel.uiState.UiState.ERROR
import com.example.spendwise.presentation.viewModel.uiState.UiState.IDLE
import com.example.spendwise.presentation.viewModel.uiState.UiState.LOADING
import com.example.spendwise.presentation.viewModel.uiState.UiState.SUCCESS
import com.example.spendwise.ui.theme.SpendWiseTheme

val LocalHomeTabScreenState = compositionLocalOf<HomeTabScreenState> {
    error("LocalHomeTabScreenState not provided")
}

@RequiresApi(value = Build.VERSION_CODES.O)
@Composable
fun HomeTabScreen(viewModel: ExpenseViewModel) {
    val uiInteractionState by viewModel.expenseUiInteractionState.collectAsState()
    val uiState = uiInteractionState.expenseEntryScreenUiState
    val selectedDate = uiInteractionState.selectedDateText
    val context = LocalContext.current
    val todayTotalSpend = uiInteractionState.todayTotalSpent

    val title = remember { mutableStateOf("") }
    val amountText = remember { mutableStateOf("") }
    val selectedCategory = remember { mutableStateOf<ExpenseCategoryEnum?>(null) }
    val notes = remember { mutableStateOf("") }
    val selectedDateInMillis = remember { mutableStateOf<Long?>(null) }
    val receiptImageUrl = remember { mutableStateOf("") }
    val isTitleError = remember { mutableStateOf(false) }
    val isAmountError = remember { mutableStateOf(false) }
    val isSelectedCategoryError = remember { mutableStateOf(false) }
    val isDateError = remember { mutableStateOf(false) }
    val successAnimationVisible = remember { mutableStateOf(false) }

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.success_animation)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        isPlaying = successAnimationVisible.value,
        speed = 1.5f,
        restartOnPlay = false
    )

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            receiptImageUrl.value = it.toString()
        }
    }

    val state = remember {
        HomeTabScreenState(
            title = title,
            isTitleError = isTitleError,
            amountText = amountText,
            isAmountError = isAmountError,
            selectedCategory = selectedCategory,
            notes = notes,
            selectedDateInMillis = selectedDateInMillis,
            receiptImageUrl = receiptImageUrl,
            isSelectedCategoryError = isSelectedCategoryError,
            isDateError = isDateError,
        )
    }

    CompositionLocalProvider(value = LocalHomeTabScreenState provides state) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {

                when (uiState) {
                    is LOADING -> Box(modifier = Modifier.fillMaxSize()) { CircularProgressIndicator() }
                    is SUCCESS -> {
                        successAnimationVisible.value = true
                        LaunchedEffect(Unit) {
                            Toast.makeText(context, "Expense added", Toast.LENGTH_SHORT).show()
                            kotlinx.coroutines.delay(2500) // show animation for 2 seconds
                            successAnimationVisible.value = false
                            viewModel.updateExpenseEntryScreenUiState(IDLE)
                        }
                    }

                    is ERROR -> {
                        Toast.makeText(context, uiState.errorMsg, Toast.LENGTH_SHORT).show()
                        viewModel.updateExpenseEntryScreenUiState(IDLE)
                    }

                    IDLE -> {}
                }

                TodayTotalSpend(
                    modifier = Modifier.padding(all = 20.dp),
                    todayTotalSpent = todayTotalSpend,
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Add a new Expense",
                    fontWeight = FontWeight.Bold,
                )

                TitleInput()
                AmountInput()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    CategoryDropdown(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                    )

                    DateSelector(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        dateText = selectedDate,
                    ) { dateInMillis ->
                        dateInMillis?.let { viewModel.updateSelectedDate(dateInMillis = it) }
                    }
                }

                NotesInput()

                ImagePickerSection {
                    imagePicker.launch("image/*")
                }

                Spacer(modifier = Modifier.weight(1f))

                SubmitButton(viewModel = viewModel)
            }

            AnimatedVisibility(
                visible = successAnimationVisible.value,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(150.dp)
                )
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.updateTodayTotalSpend()
    }
}

@Preview
@Composable
fun ExpenseEntryScreenPreview() {
    SpendWiseTheme {
        //ExpenseEntryScreen()
    }
}