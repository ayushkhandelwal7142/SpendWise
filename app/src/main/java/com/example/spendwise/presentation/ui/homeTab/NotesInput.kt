package com.example.spendwise.presentation.ui.homeTab

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val MAX_CHARS = 100

@Composable
fun NotesInput() {
    val state = LocalExpenseEntryState.current
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(horizontal = 32.dp, vertical = 4.dp),
        value = state.notes.value,
        onValueChange = {
            if (it.length <= MAX_CHARS) {
                state.notes.value = it
            }
        },
        label = { Text("Notes (Optional)") },
        placeholder = { Text("Max 100 chars") },
    )
}
