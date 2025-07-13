package com.example.spendwise.presentation.viewModel.useCase

import android.content.Context
import androidx.core.content.edit

class AppLaunchUseCase {

    companion object {
        fun isFirstTimeLaunch(context: Context): Boolean {
            val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            return prefs.getBoolean("first_time", true)
        }

        fun setFirstTimeLaunchDone(context: Context) {
            val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            prefs.edit { putBoolean("first_time", false) }
        }
    }
}