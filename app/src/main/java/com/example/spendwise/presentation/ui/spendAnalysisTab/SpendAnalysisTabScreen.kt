package com.example.spendwise.presentation.ui.spendAnalysisTab

import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap
import com.example.spendwise.presentation.viewModel.ExpenseViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SpendAnalysisTabScreen(
    viewModel: ExpenseViewModel,
) {
    val context = LocalContext.current
    val uiInteractionState by viewModel.expenseUiInteractionState.collectAsState()
    val categoryWiseSpend = uiInteractionState.categoryWiseSpend
    val dailyTotalSpend = uiInteractionState.todayTotalSpent

    val pdfExportView = remember { mutableStateOf<View?>(null) }
    val chartBitmap = remember { mutableStateOf<Bitmap?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val captureModifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)

        AndroidView(
            modifier = captureModifier,
            factory = { ctx ->
                createSpendAnalysisExportableView(
                    context = ctx,
                    spendTotal = dailyTotalSpend,
                    categoryWiseSpend = categoryWiseSpend ?: emptyList()
                ).apply {
                    pdfExportView.value = this
                }
            }
        )

        Text(text = "Category-wise Total Spends:")

        categoryWiseSpend?.let {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                factory = { ctx ->
                    SpendAnalysisUseCase().createSpendAnalysisChartView(ctx, it).apply {
                        post {
                            try {
                                chartBitmap.value = this.drawToBitmap()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    SpendAnalysisUseCase().exportComposableAsPdf(
                        context = context,
                        viewModel = viewModel,
                        spendTotal = dailyTotalSpend,
                        categoryWiseSpend = categoryWiseSpend ?: emptyList()
                    )
                }
            ) { Text("Export to PDF") }

            Spacer(modifier = Modifier.padding(horizontal = 4.dp))

            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    val bitmap = chartBitmap.value
                    if (bitmap != null) {
                        val uri = SpendAnalysisUseCase().saveBitmapToCache(context, bitmap)
                        if (uri != null) {
                            SpendAnalysisUseCase().shareImage(context, uri)
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Error capturing chart screenshot",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) { Text("Share Chart") }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getTotalAmountCategoryWise()
    }
}

