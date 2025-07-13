package com.example.spendwise.presentation.ui.spendAnalysisTab

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.spendwise.data.roomDb.model.CategoryWiseSpendModel

fun createSpendAnalysisExportableView(
    context: Context,
    spendTotal: Double,
    categoryWiseSpend: List<CategoryWiseSpendModel>
): View {
    val container = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(24, 24, 24, 24)
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
    }

    val title = TextView(context).apply {
        text = "SpendWise Report"
        textSize = 20f
        gravity = Gravity.CENTER
        setPadding(0, 0, 0, 16)
        setTextColor(ContextCompat.getColor(context, android.R.color.black))
    }
    container.addView(title)

    val totalSpend = TextView(context).apply {
        text = "Total Expense: ₹${String.format("%.2f", spendTotal)}"
        textSize = 16f
        setPadding(0, 0, 0, 16)
        setTextColor(ContextCompat.getColor(context, android.R.color.black))
    }
    container.addView(totalSpend)

    val categoryTitle = TextView(context).apply {
        text = "Category-wise Spend:"
        textSize = 16f
        setPadding(0, 0, 0, 8)
        setTextColor(ContextCompat.getColor(context, android.R.color.black))
    }
    container.addView(categoryTitle)

    categoryWiseSpend.forEach {
        val categoryItem = TextView(context).apply {
            text = "- ${it.category}: ₹${String.format("%.2f", it.totalAmount)}"
            textSize = 14f
            setPadding(0, 0, 0, 4)
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
        }
        container.addView(categoryItem)
    }

    return container
}
