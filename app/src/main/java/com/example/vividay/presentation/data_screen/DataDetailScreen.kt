package com.example.vividay.presentation.data_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vividay.models.Day

// I want to display this page when the DataBox is clicked from DataScreen
@Composable
fun DataDetailScreen(day: Day) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Display details
        Text(modifier = Modifier
            .padding(20.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            text = "Date: ${day.dateTime}")
        Text(modifier = Modifier
            .padding(20.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            text = "Color: ${day.dayColor}")
        Text(modifier = Modifier
            .padding(20.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            text = "Note: ${day.dayNote}")
    }
}
