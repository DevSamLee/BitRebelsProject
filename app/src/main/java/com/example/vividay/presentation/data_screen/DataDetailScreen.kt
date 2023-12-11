package com.example.vividay.presentation.data_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vividay.models.Day
import androidx.navigation.NavHostController

@Composable
fun DataDetailScreen(navController: NavHostController, day: Day) {
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
            text = "Mood: ${getColorText(day.dayColor)}")
        Text(modifier = Modifier
            .padding(20.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            text = "Note: ${day.dayNote}")
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(text = "Back to Home")
        }
    }
}

// Function to map hex code to color text
fun getColorText(hexCode: String?): String {
    return when (hexCode) {
        "#42eff5" -> "Great"
        "#2267f2" -> "Good"
        "#12c45c" -> "SoSo"
        "#f59e42" -> "Bad"
        "#f54e42" -> "Terrible"
        else -> "Unknown"
    }
}