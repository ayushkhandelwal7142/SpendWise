package com.example.spendwise.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.spendwise.data.roomDb.repository.ExpenseDataRepository
import com.example.spendwise.data.roomDb.database.ExpenseDatabase
import com.example.spendwise.domain.ExpenseUseCase
import com.example.spendwise.presentation.model.BottomNavItem
import com.example.spendwise.presentation.ui.homeTab.HomeTabScreen
import com.example.spendwise.presentation.ui.expenseTab.ExpenseTabScreen
import com.example.spendwise.presentation.ui.ViewSpendAnalysisScreen
import com.example.spendwise.presentation.viewModel.ExpenseViewModel
import com.example.spendwise.ui.theme.SpendWiseTheme

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val expenseDao = ExpenseDatabase.buildDatabase(this)!!.getExpenseDao
        val repository = ExpenseDataRepository(expenseDao = expenseDao)
        val useCase = ExpenseUseCase()
        val viewModel = ExpenseViewModel(
            expenseUseCase = useCase,
            expenseDataRepository = repository,
        )

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val items = listOf(
                BottomNavItem.Home,
                BottomNavItem.Expense,
                BottomNavItem.Analysis
            )

            SpendWiseTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            val currentRoute =
                                navController.currentBackStackEntryAsState().value?.destination?.route
                            items.forEach { item ->
                                NavigationBarItem(
                                    selected = currentRoute == item.route,
                                    onClick = { navController.navigate(item.route) },
                                    icon = { Icon(item.icon, contentDescription = item.label) },
                                    label = { Text(item.label) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = BottomNavItem.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(route = BottomNavItem.Home.route) { HomeScreen(viewModel = viewModel) }
                        composable(route = BottomNavItem.Expense.route) { ExpenseScreen(viewModel = viewModel) }
                        composable(route = BottomNavItem.Analysis.route) {
                            SpendAnalysisScreen(
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun HomeScreen(viewModel: ExpenseViewModel) {
        HomeTabScreen(viewModel = viewModel)
    }

    @Composable
    fun ExpenseScreen(viewModel: ExpenseViewModel) {
        ExpenseTabScreen(viewModel = viewModel)
    }


    @Composable
    fun SpendAnalysisScreen(viewModel: ExpenseViewModel) {
        ViewSpendAnalysisScreen(
            viewModel = viewModel,
        )

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpendWiseTheme {
        //ExpenseEntryScreen()
    }
}