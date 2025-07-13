package com.example.spendwise.presentation.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.spendwise.data.roomDb.model.CategoryWiseSpendModel
import com.example.spendwise.presentation.viewModel.ExpenseViewModel
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewSpendAnalysisScreen(
    viewModel: ExpenseViewModel,
) {
    val uiInteractionState by viewModel.expenseUiInteractionState.collectAsState()
    val categoryWiseSpend = uiInteractionState.categoryWiseSpend
    val dailyTotalSpend = uiInteractionState.todayTotalSpent

    val context = LocalContext.current
    val density = LocalDensity.current
    val pdfExportRef = remember { mutableStateOf<ImageBitmap?>(null) }

    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val viewCaptureModifier = Modifier
        .graphicsLayer()
        .onGloballyPositioned {
            // Will be used in drawToBitmap
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 📸 Wrap exportable content in a Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        // Optional overlays
                    }
                }
                .onGloballyPositioned { layoutCoordinates ->
                    // Take screenshot only on click
                }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Today's Total Expense = $dailyTotalSpend",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.padding(vertical = 8.dp))

                Text(text = "Category-wise Total Spends:", fontWeight = FontWeight.Bold)

                categoryWiseSpend?.let {
                    CategoryBarChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        categoryMap = categoryWiseSpend,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    // 📦 Export to PDF
                    exportComposableAsPdf(
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
                    // TODO: Share logic
                }
            ) { Text("Share") }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getTotalAmountCategoryWise()
    }
}


fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri? {
    return try {
        val imagesFolder = File(context.cacheDir, "images")
        imagesFolder.mkdirs()
        val file = File(imagesFolder, "shared_chart.png")

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
        }

        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun shareImage(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Share Screenshot"))
}

fun exportComposableAsPdf(
    context: Context,
    viewModel: ExpenseViewModel,
    spendTotal: Double,
    categoryWiseSpend: List<CategoryWiseSpendModel>
) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size

    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    val paint = Paint().apply {
        textSize = 12f
        isAntiAlias = true
    }

    var yPosition = 50

    canvas.drawText("SpendWise Report", 200f, yPosition.toFloat(), paint)
    yPosition += 40
    canvas.drawText("Total Expense: ₹${String.format("%.2f", spendTotal)}", 50f, yPosition.toFloat(), paint)
    yPosition += 30
    canvas.drawText("Category-wise Spend:", 50f, yPosition.toFloat(), paint)
    yPosition += 30

    categoryWiseSpend.forEach {
        canvas.drawText("- ${it.category}: ₹${String.format("%.2f", it.totalAmount)}", 70f, yPosition.toFloat(), paint)
        yPosition += 25
    }

    pdfDocument.finishPage(page)

    try {
        val file = File(context.cacheDir, "SpendReport.pdf")
        pdfDocument.writeTo(FileOutputStream(file))
        Toast.makeText(context, "PDF exported to ${file.absolutePath}", Toast.LENGTH_SHORT).show()

        // 🔁 Optional: Share intent
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share PDF"))
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error exporting PDF", Toast.LENGTH_SHORT).show()
    } finally {
        pdfDocument.close()
    }
}



