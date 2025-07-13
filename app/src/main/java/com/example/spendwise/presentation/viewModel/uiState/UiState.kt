package com.example.spendwise.presentation.viewModel.uiState

sealed class UiState {
    data object IDLE: UiState()
    data object LOADING: UiState()
    data object SUCCESS: UiState()
    data class ERROR(val errorMsg: String): UiState()
}