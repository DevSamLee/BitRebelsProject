package com.example.vividay.presentation.data_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vividay.models.Day
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    onSaved: () -> Unit
)  {
    val database = Firebase.database
    var dateTime by remember {
        mutableStateOf("")
    }
    var dayColor by remember {
        mutableStateOf("")
    }
    var dayNote by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(330.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Add a text above the TextFields
            Text(
                modifier = Modifier
                    .padding(20.dp),
                text = "How's your day today?",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            TextField(
                modifier = Modifier
                    .width(330.dp),
                value = dateTime,
                onValueChange = { newText -> dateTime = newText },
                label = { Text(text = "Date") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextField(
                modifier = Modifier
                    .width(330.dp),
                value = dayColor,
                onValueChange = { newText -> dayColor = newText },
                label = { Text(text = "Color") }
            )
            TextField(
                value = dayNote,
                onValueChange = { newText -> dayNote = newText },
                label = { Text(text = "Note") },
                modifier = Modifier
                    .height(330.dp)
                    .width(330.dp)
            )
            Spacer(modifier = Modifier.weight(1f)) // Spacer to push content above bottom navigation bar
            val context = LocalContext.current
            Button(
                onClick = {
                    val daysRef = database.reference.child("Day")
                    val dayRef = daysRef.child(dateTime)
                    val dayInfo = Day(dateTime, dayColor, dayNote)
                    dayRef.setValue(dayInfo)
                    Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show()
                    dateTime = ""
                    dayColor = ""
                    dayNote = ""
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Save")
            }
        }
    }
}