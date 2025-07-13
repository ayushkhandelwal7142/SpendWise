package com.example.spendwise.presentation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label:String,
    val icon: ImageVector,
) {
    data object Home : BottomNavItem(route = "home", label = "Home", icon = Icons.Default.Home)
    data object Expense : BottomNavItem(route = "expense", label = "Expenses", icon = Icons.Default.Info)
    data object Analysis : BottomNavItem(route = "analysis", label = "Analysis", icon = Icons.Default.CheckCircle)
}