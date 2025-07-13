package com.example.spendwise.presentation.ui.homeTab

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TitleInput() {
    val state = LocalHomeTabScreenState.current
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        value = state.title.value,
        onValueChange = {
            state.title.value = it
            if (it.isNotEmpty()) { state.isTitleError.value = false }
        },
        label = { Text("Title") },
        placeholder = { Text("Enter Expense Title") },
        isError = state.isTitleError.value,
        supportingText = {
            if (state.isTitleError.value) Text(text = "Please enter title")
        }
    )
}

