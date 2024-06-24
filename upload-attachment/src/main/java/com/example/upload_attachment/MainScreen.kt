package com.example.upload_attachment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {  },
        sheetState = bottomSheetState,
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
    ) {

        SheetContent()

    }
}

@Composable
private fun SheetContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .clickable { },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_image_24),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Выбрать картинку из галереи"
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .clickable { },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_image_24),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Выбрать Видео из галереи"
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .clickable { },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_image_24),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Выбрать Файл"
            )
        }
    }
}

@Composable
@Preview()
fun SheetContentPreview() {
    SheetContent()
}