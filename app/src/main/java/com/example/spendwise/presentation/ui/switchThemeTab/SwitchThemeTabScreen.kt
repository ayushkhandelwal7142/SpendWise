package com.example.spendwise.presentation.ui.switchThemeTab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ThemeScreen(isDarkTheme: Boolean, toggleTheme: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Current Theme: ${if (isDarkTheme) "Dark" else "Light"}")
        Spacer(modifier = Modifier.padding(12.dp))
        Button(onClick = toggleTheme) {
            Text("Switch to ${if (isDarkTheme) "Light" else "Dark"} Theme")
        }
    }
}
