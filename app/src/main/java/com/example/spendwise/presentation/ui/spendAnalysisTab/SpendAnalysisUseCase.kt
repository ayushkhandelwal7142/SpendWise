package com.example.spendwise.presentation.ui.spendAnalysisTab

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.spendwise.data.roomDb.model.CategoryWiseSpendModel
import com.example.spendwise.presentation.viewModel.ExpenseViewModel
import java.io.File
import java.io.FileOutputStream
import kotlin.collections.forEach

class SpendAnalysisUseCase {
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
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4

        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val paint = Paint().apply {
            textSize = 12f
            isAntiAlias = true
            color = Color.Black.toArgb()
        }

        var y = 50
        canvas.drawText("SpendWise Report", 200f, y.toFloat(), paint)
        y += 40
        canvas.drawText(
            "Today's Total Expense: ₹${String.format("%.2f", spendTotal)}",
            50f,
            y.toFloat(),
            paint
        )
        y += 30
        canvas.drawText("Category-wise Overall Spend:", 50f, y.toFloat(), paint)
        y += 30

        categoryWiseSpend.forEach {
            canvas.drawText(
                "- ${it.category}: ₹${String.format("%.2f", it.totalAmount)}",
                70f,
                y.toFloat(),
                paint
            )
            y += 25
        }

        pdfDocument.finishPage(page)

        try {
            val file = File(context.cacheDir, "SpendReport.pdf")
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(context, "PDF exported to ${file.absolutePath}", Toast.LENGTH_SHORT)
                .show()

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

    fun createSpendAnalysisChartView(
        context: Context,
        categoryWiseSpend: List<CategoryWiseSpendModel>
    ): View {
        return androidx.compose.ui.platform.ComposeView(context).apply {
            setContent {
                androidx.compose.material3.Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CategoryBarChart(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            categoryMap = categoryWiseSpend
                        )
                    }
                }
            }
        }
    }
}