package com.example.spendwise.presentation.ui.homeTab

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AmountInput() {
    val state = LocalHomeTabScreenState.current
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        value = state.amountText.value,
        onValueChange = {
            state.amountText.value = it
            if (it.isNotEmpty()) {
                state.isAmountError.value = false
            }
        },
        label = { Text("Amount") },
        placeholder = { Text("Enter amount") },
        isError = state.isAmountError.value,
        supportingText = {
            if (state.isAmountError.value) Text(text = "Please enter amount")
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}
