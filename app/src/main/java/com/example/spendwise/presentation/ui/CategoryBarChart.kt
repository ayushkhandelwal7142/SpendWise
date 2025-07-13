package com.example.spendwise.presentation.ui

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.spendwise.data.roomDb.model.CategoryWiseSpendModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun CategoryBarChart(
    categoryMap: List<CategoryWiseSpendModel>,
    modifier: Modifier = Modifier
) {
    val entries = categoryMap.mapIndexed { index, entry ->
        BarEntry(index.toFloat(), entry.totalAmount.toFloat())
    }

    val labels = categoryMap.map { entry ->
        entry.category
    }

    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    valueFormatter = IndexAxisValueFormatter(labels)
                    textColor = Color.BLACK
                    setDrawGridLines(false)
                }

                axisLeft.apply {
                    textColor = Color.BLACK
                    axisMinimum = 0f
                }

                axisRight.isEnabled = false

                val dataSet = BarDataSet(entries, "Expenses").apply {
                    colors = listOf(
                        Color.rgb(244, 67, 54),
                        Color.rgb(33, 150, 243),
                        Color.rgb(76, 175, 80),
                        Color.rgb(255, 193, 7)
                    )
                }

                data = BarData(dataSet).apply {
                    barWidth = 0.9f
                }

                invalidate()
            }
        },
        modifier = modifier
    )
}

