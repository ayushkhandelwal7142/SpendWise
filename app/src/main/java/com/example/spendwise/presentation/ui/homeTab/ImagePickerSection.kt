package com.example.spendwise.presentation.ui.homeTab

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.spendwise.R

@Composable
fun ImagePickerSection(
    onPickImage: () -> Unit,
) {
    val state = LocalExpenseEntryState.current
    val painter = rememberAsyncImagePainter(model = state.receiptImageUrl.value)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .height(120.dp)
                .clickable { onPickImage() }
                .padding(4.dp)
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(size = 4.dp),
                    color = Color.Black,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Upload Receipt",
                fontSize = 14.sp,
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_drop_down),
                contentDescription = null,
            )
        }

        Image(
            modifier = Modifier
                .height(120.dp)
                .weight(1f)
                .padding(4.dp)
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(4.dp)),
            painter = painter,
            contentDescription = null,
        )
    }
}