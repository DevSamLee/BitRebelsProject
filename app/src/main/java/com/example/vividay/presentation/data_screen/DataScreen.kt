package com.example.vividay.presentation.data_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vividay.models.Day
import com.example.vividay.sealed.DataState
import com.example.vividay.sealed.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataScreen(
    viewModel: DataViewModel,
    navHostController: NavHostController
) {
    when (val result = viewModel.response.value) {
        is DataState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is DataState.Success -> {
            var selectedDay by remember { mutableStateOf<Day?>(null) }
            val state = rememberLazyStaggeredGridState(
                initialFirstVisibleItemIndex = 99
            )
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                state = state,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalItemSpacing = 16.dp
            ) {
                items(result.data ?: emptyList()) { day ->
                    DataBox(day = day, navController = navHostController) {
                        selectedDay = day // Set the selectedDay when the box is clicked
                    }
                }
            }
            // NavHost for detailed screen
            val detailNavController = rememberNavController()
            NavHost(
                navController = detailNavController,
                startDestination = "empty"
            ) {
                composable("empty") {
                    // Placeholder composable, you can replace it with an empty screen or any initial screen
                }
                composable("dataDetailScreen/{dateTime}") { backStackEntry ->
                    val dateTime = backStackEntry.arguments?.getString("dateTime")
                    if (dateTime != null) {
                        // Retrieve the detailed data based on dateTime and pass it to DataDetailScreen
                        val detailedDay = result.data?.find { it.dateTime == dateTime }
                        if (detailedDay != null) {
                            DataDetailScreen(day = detailedDay)
                        } else {
                            // Handle the case when detailed data is not found for the given dateTime
                            // You can navigate to an error screen or show a toast message
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Error: Detailed data not found",
                                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                                )
                            }
                        }
                    } else {
                        // Handle the case when dateTime is null
                        // You can navigate to an error screen or show a toast message
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Error: Invalid dateTime",
                                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                            )
                        }
                    }
                }
            }
        }
        is DataState.Failure -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = result.message,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                )
            }
        }
        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error fetching data",
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                )
            }
        }
    }
}

@Composable
fun DataBox(day: Day, navController: NavController, onBoxClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height((day.dayNote?.takeIf { it.isNotEmpty() }?.length ?: 50).dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(android.graphics.Color.parseColor(day.dayColor ?: "#808080")))
            .clickable {
                onBoxClicked()
                println("Selected Day: ${day.dateTime}")
                // Navigate to a detailed view or another screen
                navController.navigate(Screen.DataDetail.route + "/${day.dateTime}")
            }
    ) {
        Text(
            text = "${day.dateTime}",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}